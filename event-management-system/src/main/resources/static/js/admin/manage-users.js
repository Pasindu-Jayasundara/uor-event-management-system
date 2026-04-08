const MU_USERS = [
    {
        id: 'USR001', name: 'Kavindra Rajapaksa', email: 'k.rajapaksa@fot.ruh.ac.lk',
        role: 'admin', dept: 'Faculty of Technology', joined: 'Jan 10, 2026',
        lastActive: 'Today, 9:14 AM', enabled: true
    },
    {
        id: 'USR002', name: 'Nimesha Bandara', email: 'n.bandara@fot.ruh.ac.lk',
        role: 'admin', dept: 'Faculty of Technology', joined: 'Jan 12, 2026',
        lastActive: 'Yesterday, 3:30 PM', enabled: true
    },
    {
        id: 'USR003', name: 'Tharaka Wijesekara', email: 't.wijesekara@students.ruh.ac.lk',
        role: 'student', dept: 'Computer Science', joined: 'Feb 3, 2026',
        lastActive: 'Apr 7, 2026', enabled: true
    },
    {
        id: 'USR004', name: 'Sachini Madushika', email: 's.madushika@students.ruh.ac.lk',
        role: 'student', dept: 'Information Technology', joined: 'Feb 5, 2026',
        lastActive: 'Apr 6, 2026', enabled: true
    },
    {
        id: 'USR005', name: 'Roshan Kumara', email: 'r.kumara@fot.ruh.ac.lk',
        role: 'staff', dept: 'Administration', joined: 'Mar 1, 2026',
        lastActive: 'Apr 5, 2026', enabled: true
    },
    {
        id: 'USR006', name: 'Amali Seneviratne', email: 'a.seneviratne@students.ruh.ac.lk',
        role: 'student', dept: 'Engineering Technology', joined: 'Mar 14, 2026',
        lastActive: 'Mar 20, 2026', enabled: false
    },
];

/* ── state ── */
let muActiveFilter = 'all';
let muActiveUserId = null;
let muPopupAnchor = null;

/* ── helpers ── */
function muInitials(name) {
    return name.split(' ').map(w => w[0]).join('').slice(0, 2).toUpperCase();
}

function muRoleBadge(role) {
    if (role === 'admin')
        return '<span class="badge-pill badge-red-light"><span class="badge-dot dot-red"></span>Admin</span>';
    if (role === 'staff')
        return '<span class="badge-pill" style="background:rgba(130,24,26,0.08);color:#82181A"><span class="badge-dot" style="background:#82181A"></span>Staff</span>';
    return '<span class="badge-pill" style="background:#f0f0f5;color:#666"><span class="badge-dot" style="background:#aaa"></span>Student</span>';
}

function muStatusBadge(enabled) {
    return enabled
        ? '<span class="badge-pill badge-success"><span class="badge-dot dot-success"></span>Active</span>'
        : '<span class="badge-pill badge-danger"><span class="badge-dot dot-danger"></span>Disabled</span>';
}

function muBuildRow(u) {
    const dim = u.enabled ? '' : 'mu-row-disabled';
    return `<tr class="${dim}" data-uid="${u.id}">
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
                <td style="color:var(--text-muted);font-size:12px">${u.joined}</td>
                <td class="mu-td-actions">
                    <button class="mu-dots-btn" type="button"
                            onclick="muOpenPopup(event,'${u.id}')"
                            aria-label="Actions for ${u.name}">
                        <span></span><span></span><span></span>
                    </button>
                </td>
            </tr>`;
}

/* ── render ── */
function muRender() {
    const q = (document.getElementById('mu-search-input').value || '').toLowerCase().trim();
    const tbody = document.getElementById('mu-all-tbody');
    let rows = '';
    let filtered = MU_USERS.filter(u => {
        const matchFilter =
            muActiveFilter === 'all' ||
            (muActiveFilter === 'disabled' && !u.enabled) ||
            (muActiveFilter !== 'disabled' && u.role === muActiveFilter);
        return matchFilter;
    });
    filtered.forEach(u => rows += muBuildRow(u));
    tbody.innerHTML = rows;

    // update stats
    document.getElementById('mu-stat-total').textContent = MU_USERS.length;
    document.getElementById('mu-stat-admins').textContent = MU_USERS.filter(u => u.role === 'admin').length;
    document.getElementById('mu-stat-active').textContent = MU_USERS.filter(u => u.enabled).length;
    document.getElementById('mu-stat-disabled').textContent = MU_USERS.filter(u => !u.enabled).length;
    document.getElementById('mu-acc-count').textContent = MU_USERS.length + ' users';
}

/* ── search ── */
function muHandleSearch(val) {
    const q = val.toLowerCase().trim();
    document.getElementById('mu-search-clear').style.display = q ? 'flex' : 'none';

    if (!q) {
        document.getElementById('mu-results-panel').style.display = 'none';
        document.getElementById('mu-main-card').style.display = 'block';
        return;
    }

    document.getElementById('mu-main-card').style.display = 'none';
    document.getElementById('mu-results-panel').style.display = 'block';

    const matches = MU_USERS.filter(u =>
        u.name.toLowerCase().includes(q) ||
        u.email.toLowerCase().includes(q) ||
        u.role.includes(q) ||
        u.dept.toLowerCase().includes(q)
    );

    document.getElementById('mu-results-label').textContent =
        matches.length
            ? matches.length + ' result' + (matches.length !== 1 ? 's' : '') + ' for "' + val + '"'
            : 'No results';

    const tbody = document.getElementById('mu-results-tbody');
    tbody.innerHTML = matches.map(u => muBuildRow(u)).join('');

    document.getElementById('mu-no-results').style.display = matches.length ? 'none' : 'block';
    document.getElementById('mu-results-table').style.display = matches.length ? 'table' : 'none';
}

function muClearSearch() {
    const inp = document.getElementById('mu-search-input');
    inp.value = '';
    muHandleSearch('');
    inp.focus();
}

/* ── filter chips ── */
function muSetFilter(el, val) {
    document.querySelectorAll('.mu-chip').forEach(c => c.classList.remove('active'));
    el.classList.add('active');
    muActiveFilter = val;
    muRender();
}

/* ── popup ── */
function muOpenPopup(evt, uid) {
    evt.stopPropagation();
    muActiveUserId = uid;
    const u = MU_USERS.find(u => u.id === uid);
    if (!u) return;

    // populate header
    document.getElementById('mu-popup-av').textContent = muInitials(u.name);
    document.getElementById('mu-popup-name').textContent = u.name;
    document.getElementById('mu-popup-email').textContent = u.email;

    // show/hide promote
    document.getElementById('mu-action-promote').style.display =
        u.role === 'admin' ? 'none' : 'flex';

    // toggle enable/disable
    document.getElementById('mu-action-disable').style.display = u.enabled ? 'flex' : 'none';
    document.getElementById('mu-action-enable').style.display = u.enabled ? 'none' : 'flex';

    // position near button
    const btn = evt.currentTarget;
    const rect = btn.getBoundingClientRect();
    const popup = document.getElementById('mu-popup');
    popup.classList.add('visible');
    document.getElementById('mu-popup-overlay').classList.add('visible');

    // smart position
    const pw = 240;
    let left = rect.right - pw;
    if (left < 8) left = 8;
    let top = rect.bottom + 6;
    if (top + 180 > window.innerHeight) top = rect.top - 180;
    popup.style.left = left + 'px';
    popup.style.top = top + 'px';
}

function muClosePopup() {
    document.getElementById('mu-popup').classList.remove('visible');
    document.getElementById('mu-popup-overlay').classList.remove('visible');
}

function muActionView() {
    muClosePopup();
    muOpenModal(muActiveUserId);
}

function muActionPromote() {
    const u = MU_USERS.find(u => u.id === muActiveUserId);
    if (!u || u.role === 'admin') return;
    u.role = 'admin';
    muClosePopup();
    muRender();
    muHandleSearch(document.getElementById('mu-search-input').value);
    muToast(u.name + ' promoted to Admin');
}

function muActionToggle() {
    const u = MU_USERS.find(u => u.id === muActiveUserId);
    if (!u) return;
    u.enabled = !u.enabled;
    muClosePopup();
    muRender();
    muHandleSearch(document.getElementById('mu-search-input').value);
    muToast(u.name + (u.enabled ? ' account enabled' : ' account disabled'));
}

/* ── modal ── */
function muOpenModal(uid) {
    const u = MU_USERS.find(u => u.id === uid);
    if (!u) return;

    document.getElementById('mu-modal-av').textContent = muInitials(u.name);
    document.getElementById('mu-modal-uname').textContent = u.name;
    document.getElementById('mu-modal-uemail').textContent = u.email;
    document.getElementById('mu-modal-id').textContent = u.id;
    document.getElementById('mu-modal-role').textContent = u.role.charAt(0).toUpperCase() + u.role.slice(1);
    document.getElementById('mu-modal-dept').textContent = u.dept;
    document.getElementById('mu-modal-joined').textContent = u.joined;
    document.getElementById('mu-modal-last').textContent = u.lastActive;
    document.getElementById('mu-modal-status').innerHTML = muStatusBadge(u.enabled);

    document.getElementById('mu-modal-badges').innerHTML =
        muRoleBadge(u.role) + ' ' + muStatusBadge(u.enabled);

    // modal quick actions
    let acts = '';
    if (u.role !== 'admin')
        acts += `<button class="btn-action btn-promote" type="button" onclick="muModalPromote('${u.id}')">
                            <i class="bi bi-shield-check"></i> Promote to Admin</button>`;
    if (u.enabled)
        acts += `<button class="btn-action btn-disable" type="button" onclick="muModalToggle('${u.id}')">
                            Disable Account</button>`;
    else
        acts += `<button class="btn-action btn-enable" type="button" onclick="muModalToggle('${u.id}')">
                            Enable Account</button>`;
    document.getElementById('mu-modal-actions').innerHTML = acts;

    document.getElementById('mu-modal-backdrop').classList.add('visible');
    document.getElementById('mu-modal').classList.add('visible');
}

function muCloseModal() {
    document.getElementById('mu-modal-backdrop').classList.remove('visible');
    document.getElementById('mu-modal').classList.remove('visible');
}

function muModalPromote(uid) {
    const u = MU_USERS.find(u => u.id === uid);
    if (!u) return;
    u.role = 'admin';
    muCloseModal();
    muRender();
    muHandleSearch(document.getElementById('mu-search-input').value);
    muToast(u.name + ' promoted to Admin');
}

function muModalToggle(uid) {
    const u = MU_USERS.find(u => u.id === uid);
    if (!u) return;
    u.enabled = !u.enabled;
    muCloseModal();
    muRender();
    muHandleSearch(document.getElementById('mu-search-input').value);
    muToast(u.name + (u.enabled ? ' enabled' : ' disabled'));
}

/* ── toast ── */
function muToast(msg) {
    let t = document.getElementById('mu-toast');
    if (!t) {
        t = document.createElement('div');
        t.id = 'mu-toast';
        t.style.cssText =
            'position:fixed;bottom:22px;right:22px;background:#2c1a1a;color:#fff;' +
            'font-size:12px;padding:10px 18px;border-radius:10px;z-index:999;' +
            'opacity:0;transform:translateY(8px);transition:all 0.25s;pointer-events:none;' +
            'font-family:inherit;';
        document.body.appendChild(t);
    }
    t.textContent = msg;
    t.style.opacity = '1';
    t.style.transform = 'translateY(0)';
    clearTimeout(t._tid);
    t._tid = setTimeout(() => {
        t.style.opacity = '0';
        t.style.transform = 'translateY(8px)';
    }, 2600);
}

/* ── accordion ── */
function toggleAcc(btn, bodyId) {
    btn.classList.toggle('open');
    const body = document.getElementById(bodyId);
    if (body) body.classList.toggle('visible');
}

/* ── close popup on outside click ── */
document.addEventListener('keydown', e => {
    if (e.key === 'Escape') {
        muClosePopup();
        muCloseModal();
    }
});

/* ── init ── */
muRender();
