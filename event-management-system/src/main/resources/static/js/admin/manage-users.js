const csrfToken  = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

let muActiveFilter = 'all';
let muActiveUserId = null;
let muDebounceTimer = null;

/* ════════════════════════════════════════════
   BACKEND FETCH
   GET /admin/users?q=alice&role=staff
   - q    : search text (omitted if empty)
   - role : omitted when "all", "disabled" sends enabled=false instead
   ════════════════════════════════════════════ */
async function muFetchUsers(q, filter) {
    const params = new URLSearchParams();
    if (q)                          params.set('q', q);
    if (filter === 'disabled')      params.set('enabled', 'false');
    else if (filter !== 'all')      params.set('role', 'ROLE_' + filter.toUpperCase());

    /* Expected JSON shape:
       [
         {
           "id":         "u-001",
           "name":       "Alice Smith",
           "email":      "alice@uni.edu",
           "role":       "admin" | "staff" | "student",
           "dept":       "Computer Science",
           "joined":     "Jan 12, 2024",
           "lastActive": "Apr 07, 2026",
           "enabled":    true
         }, ...
       ]
    */
    const res = await fetch('/admin/search?' + params.toString(), {
        headers: { 'Accept': 'application/json' },
        cache: 'no-store'
    });
    if (!res.ok) throw new Error('HTTP ' + res.status);
    return res.json();
}

/* ════════════════════════════════════════════
   CORE — run a search and render results
   ════════════════════════════════════════════ */
async function _muDoSearch() {
    const inp = document.getElementById('mu-search-input');
    const val = (inp.value || '').trim();
    const q   = val.toLowerCase();

    // clear ✕ button
    document.getElementById('mu-search-clear').style.display =
        val ? 'inline-flex' : 'none';

    // "all" chip + empty input → restore default accordion view
    if (!val && muActiveFilter === 'all') {
        document.getElementById('mu-main-card').style.display     = '';
        document.getElementById('mu-results-panel').style.display = 'none';
        return;
    }

    // show results panel, hide accordion
    document.getElementById('mu-main-card').style.display     = 'none';
    document.getElementById('mu-results-panel').style.display = 'block';
    document.getElementById('mu-results-label').textContent   = 'Searching…';
    document.getElementById('mu-results-tbody').innerHTML     = '';
    document.getElementById('mu-no-results').style.display    = 'none';
    document.getElementById('mu-results-table').style.display = 'none';

    let users = [];
    try {
        users = await muFetchUsers(val, muActiveFilter);
        JSON.stringify("users"+ users)
    } catch (err) {
        console.error(err);
        muToast('Search failed. Please try again.');
        document.getElementById('mu-results-label').textContent = 'Error — could not load results';
        return;
    }

    // label
    const filterLabel = muActiveFilter !== 'all'
        ? ' · ' + muActiveFilter.charAt(0).toUpperCase() + muActiveFilter.slice(1)
        : '';
    document.getElementById('mu-results-label').textContent =
        users.length
            ? users.length + ' result' + (users.length !== 1 ? 's' : '') +
            (val ? ' for "' + val + '"' : '') + filterLabel
            : 'No results' + (val ? ' for "' + val + '"' : '') + filterLabel;

    // rows
    const tbody = document.getElementById('mu-results-tbody');
    tbody.innerHTML = users.map(u => muBuildRow(u)).join('');

    document.getElementById('mu-no-results').style.display    = users.length ? 'none'  : 'block';
    document.getElementById('mu-results-table').style.display = users.length ? 'table' : 'none';
}

/* ════════════════════════════════════════════
   TRIGGERS
   ════════════════════════════════════════════ */

/* Called from onkeydown — debounced so we don't fire on every keystroke */
function muHandleSearch(evt) {
    clearTimeout(muDebounceTimer);
    muDebounceTimer = setTimeout(_muDoSearch, 350);
}

/* Clear button — immediate */
function muClearSearch() {
    const inp = document.getElementById('mu-search-input');
    inp.value = '';
    inp.focus();
    clearTimeout(muDebounceTimer);
    _muDoSearch();
}

/* Chip click — immediate, always fires regardless of search text */
function muSetFilter(el, val) {
    document.querySelectorAll('.mu-chip').forEach(c => c.classList.remove('active'));
    el.classList.add('active');
    muActiveFilter = val;
    clearTimeout(muDebounceTimer);   // cancel any pending debounce
    _muDoSearch();
}

/* ════════════════════════════════════════════
   HELPERS
   ════════════════════════════════════════════ */
function muInitials(name) {
    return (name || '').split(' ').map(w => w[0]).join('').slice(0, 2).toUpperCase();
}

function muRoleBadge(role) {
    if (role == 'role_admin')
        return '<span class="badge-pill badge-red-light"><span class="badge-dot dot-red"></span>Admin</span>';
    if (role == 'role_staff')
        return '<span class="badge-pill" style="background:rgba(130,24,26,0.08);color:#82181A"><span class="badge-dot" style="background:#82181A"></span>Staff</span>';
    return '<span class="badge-pill" style="background:#f0f0f5;color:#666"><span class="badge-dot" style="background:#aaa"></span>Student</span>';
}

function muStatusBadge(enabled) {
    return enabled
        ? '<span class="badge-pill badge-success"><span class="badge-dot dot-success"></span>Active</span>'
        : '<span class="badge-pill badge-danger"><span class="badge-dot dot-danger"></span>Disabled</span>';
}

function muBuildRow(u) {

    console.log("u: "+JSON.stringify(u))

    const dim = u.enabled ? '' : 'mu-row-disabled';
    return `<tr class="${dim}" data-uid="${u.id}" data-role="${u.role}" data-enabled="${u.enabled}">
                <td>
                    <div class="td-person">
                        <span class="avatar">${muInitials(u.name)}</span>
                        <div>
                            <div class="person-name">${u.name}</div>
                            <div class="person-email">${u.email}</div>
                        </div>
                    </div>
                </td>
                <td>${muRoleBadge(u.role)}</td>
                <td>${muStatusBadge(u.enabled)}</td>
                <td class="mu-td-actions">
                    <button class="mu-dots-btn" type="button"
                            onclick="muOpenPopup(event,'${u.id}')"
                            aria-label="Actions for ${u.name}">
                        <span></span><span></span><span></span>
                    </button>
                </td>
            </tr>`;
}

/* Re-run the current search after an action (promote / toggle) so the
   results panel reflects the change without the user having to retype. */
function muRefresh() {
    clearTimeout(muDebounceTimer);
    _muDoSearch();
}

/* ════════════════════════════════════════════
   POPUP
   ════════════════════════════════════════════ */

/* We store the last-fetched user data on the row so we don't need MU_USERS */
function muGetUserFromRow(uid) {
    const row = document.querySelector(`tr[data-uid="${uid}"]`);
    if (!row) return null;
    return {
        id:      uid,
        name:    row.querySelector('.person-name')?.textContent  || '',
        email:   row.querySelector('.person-email')?.textContent || '',
        role:    row.dataset.role,
        enabled: row.dataset.enabled === 'true' || row.dataset.enabled === '1',   // ← explicit string comparison
    };
}

function muOpenPopup(evt, uid) {
    evt.stopPropagation();
    muActiveUserId = uid;
    const u = muGetUserFromRow(uid);
    if (!u) return;

    const isAdmin   = u.role === 'role_admin';
    const isEnabled = u.enabled;

    document.getElementById('mu-popup-av').textContent    = muInitials(u.name);
    document.getElementById('mu-popup-name').textContent  = u.name;
    document.getElementById('mu-popup-email').textContent = u.email;

    // Promote: only if enabled AND not already admin
    document.getElementById('mu-action-promote').style.display =
        (!isAdmin && isEnabled) ? 'flex' : 'none';

    // Demote options: only visible if currently admin
    document.getElementById('mu-action-demote').style.display =
        isAdmin ? 'flex' : 'none';

    // Enable / Disable toggle
    document.getElementById('mu-action-disable').style.display = isEnabled ? 'flex' : 'none';
    document.getElementById('mu-action-enable').style.display  = isEnabled ? 'none' : 'flex';

    const btn   = evt.currentTarget;
    const rect  = btn.getBoundingClientRect();
    const popup = document.getElementById('mu-popup');
    const pw    = 240;
    let left = rect.right - pw;
    if (left < 8) left = 8;
    let top = rect.bottom + 6;
    if (top + 200 > window.innerHeight) top = rect.top - 200;
    popup.style.left = left + 'px';
    popup.style.top  = top  + 'px';

    popup.classList.add('visible');
    document.getElementById('mu-popup-overlay').classList.add('visible');
}

function muClosePopup() {
    document.getElementById('mu-popup').classList.remove('visible');
    document.getElementById('mu-popup-overlay').classList.remove('visible');
}

function muActionView() {
    muClosePopup();
    muOpenModal(muActiveUserId);
}

async function muActionPromote() {
    const u = muGetUserFromRow(muActiveUserId);
    if (!u || u.role === 'role_admin') return;  // was 'admin', now 'role_admin'
    muClosePopup();
    try {
        const res = await fetch(`/admin/users/${muActiveUserId}/promote`, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken   // e.g. "X-CSRF-TOKEN": "abc123..."
            }
        });
        if (!res.ok) throw new Error('HTTP ' + res.status);
        muToast(u.name + ' promoted to Admin');
        muRefresh();
    } catch (err) {
        console.error(err);
        muToast('Could not promote user. Please try again.');
    }
}

async function muActionToggle() {
    const u = muGetUserFromRow(muActiveUserId);
    if (!u) return;
    muClosePopup();
    const endpoint = u.enabled
        ? `/admin/disable-user/${muActiveUserId}`
        : `/admin/enable-user/${muActiveUserId}`;
    try {
        const res = await fetch(endpoint, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken   // e.g. "X-CSRF-TOKEN": "abc123..."
            },
            cache: "no-cache"
        });

        console.log(res)

        if (!res.ok) throw new Error('HTTP ' + res.status);
        muToast(u.name + (u.enabled ? ' account disabled' : ' account enabled'));
        muRefresh();
    } catch (err) {
        console.error(err);
        muToast('Could not update account. Please try again.');
    }
}

/* ════════════════════════════════════════════
   MODAL
   ════════════════════════════════════════════ */
async function muOpenModal(uid) {
    document.getElementById('mu-modal-backdrop').classList.add('visible');
    document.getElementById('mu-modal').classList.add('visible');

    // Show placeholders while we fetch full details
    document.getElementById('mu-modal-av').textContent     = '…';
    document.getElementById('mu-modal-uname').textContent  = 'Loading…';
    document.getElementById('mu-modal-uemail').textContent = '';
    document.getElementById('mu-modal-badges').innerHTML   = '';
    document.getElementById('mu-modal-id').textContent     = uid;
    document.getElementById('mu-modal-role').textContent   = '—';
    document.getElementById('mu-modal-dept').textContent   = '—';
    document.getElementById('mu-modal-status').innerHTML   = '—';
    document.getElementById('mu-modal-actions').innerHTML  = '';

    let u;
    try {
        const res = await fetch(`/admin/users/${uid}`, {
            headers: { 'Accept': 'application/json' }
        });
        if (!res.ok) throw new Error('HTTP ' + res.status);
        u = await res.json();
    } catch (err) {
        console.error(err);
        document.getElementById('mu-modal-uname').textContent = 'Could not load user.';
        return;
    }

    const isAdmin   = u.role === 'role_admin';
    const isEnabled = u.enabled;
    const roleClean = u.role.replace('role_', '');

    document.getElementById('mu-modal-av').textContent     = muInitials(u.name);
    document.getElementById('mu-modal-uname').textContent  = u.name;
    document.getElementById('mu-modal-uemail').textContent = u.email;
    document.getElementById('mu-modal-id').textContent     = u.id;
    document.getElementById('mu-modal-role').textContent   = roleClean.charAt(0).toUpperCase() + roleClean.slice(1);
    document.getElementById('mu-modal-dept').textContent   = u.dept || '—';
    document.getElementById('mu-modal-status').innerHTML   = muStatusBadge(u.enabled);
    document.getElementById('mu-modal-badges').innerHTML   = muRoleBadge(u.role) + ' ' + muStatusBadge(u.enabled);

    let acts = '';

    // Promote: enabled non-admins only
    if (!isAdmin && isEnabled)
        acts += `<button class="btn-action btn-promote" type="button" onclick="muModalPromote('${u.id}')">
                     <i class="bi bi-shield-check"></i> Promote to Admin</button>`;

    // Demote: admins only
    if (isAdmin)
        acts += `<button class="btn-action btn-secondary" type="button" onclick="muModalDemote('${u.id}')">
                     <i class="bi bi-arrow-down-circle"></i> Demote</button>`;

    // Enable / Disable toggle
    acts += isEnabled
        ? `<button class="btn-action btn-disable" type="button" onclick="muModalToggle('${u.id}',false)">Disable Account</button>`
        : `<button class="btn-action btn-enable"  type="button" onclick="muModalToggle('${u.id}',true)">Enable Account</button>`;

    document.getElementById('mu-modal-actions').innerHTML = acts;
}

function muCloseModal() {
    document.getElementById('mu-modal-backdrop').classList.remove('visible');
    document.getElementById('mu-modal').classList.remove('visible');
}

async function muModalPromote(uid) {
    muCloseModal();
    try {
        const res = await fetch(`/admin/users/${uid}/promote`, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken   // e.g. "X-CSRF-TOKEN": "abc123..."
            }
        });
        if (!res.ok) throw new Error('HTTP ' + res.status);
        muToast('User promoted to Admin');
        muRefresh();
    } catch (err) {
        console.error(err);
        muToast('Could not promote user. Please try again.');
    }
}

async function muModalToggle(uid, enable) {
    muCloseModal();
    const endpoint = enable
        ? `/admin/enable-user/${uid}`
        : `/admin/disable-user/${uid}`;
    try {
        const res = await fetch(endpoint, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken   // e.g. "X-CSRF-TOKEN": "abc123..."
            }
        });
        if (!res.ok) throw new Error('HTTP ' + res.status);
        muToast('Account ' + (enable ? 'enabled' : 'disabled'));
        muRefresh();
    } catch (err) {
        console.error(err);
        muToast('Could not update account. Please try again.');
    }
}

/* ════════════════════════════════════════════
   TOAST
   ════════════════════════════════════════════ */
function muToast(msg) {
    let t = document.getElementById('mu-toast');
    if (!t) {
        t = document.createElement('div');
        t.id = 'mu-toast';
        t.style.cssText =
            'position:fixed;bottom:22px;right:22px;background:#2c1a1a;color:#fff;' +
            'font-size:12px;padding:10px 18px;border-radius:10px;z-index:9999;' +
            'opacity:0;transform:translateY(8px);transition:all 0.25s;pointer-events:none;' +
            'font-family:inherit;';
        document.body.appendChild(t);
    }
    t.textContent     = msg;
    t.style.opacity   = '1';
    t.style.transform = 'translateY(0)';
    clearTimeout(t._tid);
    t._tid = setTimeout(() => {
        t.style.opacity   = '0';
        t.style.transform = 'translateY(8px)';
    }, 2600);
}

/* ════════════════════════════════════════════
   ACCORDION
   ════════════════════════════════════════════ */
function toggleAcc(btn, bodyId) {
    btn.classList.toggle('open');
    const body = document.getElementById(bodyId);
    if (body) body.classList.toggle('visible');
}

/* ════════════════════════════════════════════
   GLOBAL KEYBOARD
   ════════════════════════════════════════════ */
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') {
        muClosePopup();
        muCloseModal();
    }
});

async function muActionDemote() {
    const u = muGetUserFromRow(muActiveUserId);
    if (!u) return;
    muClosePopup();
    try {
        const res = await fetch(`/admin/users/${muActiveUserId}/demote`, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken   // e.g. "X-CSRF-TOKEN": "abc123..."
            },
            cache:'no-cache'
        });
        if (!res.ok) throw new Error('HTTP ' + res.status);
        muToast(u.name + ' demoted');
        muRefresh();
    } catch (err) {
        console.error(err);
        muToast('Could not demote user. Please try again.');
    }
}

async function muModalDemote(uid) {
    muCloseModal();
    try {
        const res = await fetch(`/admin/users/${uid}/demote`, {
            method: 'POST',
            headers: {
                [csrfHeader]: csrfToken   // e.g. "X-CSRF-TOKEN": "abc123..."
            }
        });
        if (!res.ok) throw new Error('HTTP ' + res.status);
        muToast('User demoted');
        muRefresh();
    } catch (err) {
        console.error(err);
        muToast('Could not demote user. Please try again.');
    }
}