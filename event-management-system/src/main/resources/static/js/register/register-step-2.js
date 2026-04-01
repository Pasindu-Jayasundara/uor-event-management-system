document.addEventListener('DOMContentLoaded', () => {

    departmentBasedOnFaculty();
});

function departmentBasedOnFaculty() {
    const facultySelect = document.getElementById('faculty');
    const departmentSelect = document.getElementById('department');

    // Access the data we passed from Thymeleaf
    const facultyDepartmentData = window.facultyDepartmentList || [];
    console.log(facultyDepartmentData)

    facultySelect.addEventListener('change', function () {
        const selectedFaculty = this.value;

        departmentSelect.innerHTML = '<option value="">Select a department</option>';

        if (!selectedFaculty) return;

        const departments = facultyDepartmentData.find(value => {
            return value.faculty.id == selectedFaculty
        });

        console.log(departments);

        if (departments && Array.isArray(departments.departments)) {
            departments.departments.forEach(dept => {
                const option = document.createElement('option');
                option.value = dept.id;
                option.textContent = dept.department;
                departmentSelect.appendChild(option);
            });
        }
    });

    // Optional: pre-select if editing an existing form
    if (facultySelect.value) {
        facultySelect.dispatchEvent(new Event('change'));
    }
}
