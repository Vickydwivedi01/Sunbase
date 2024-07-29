document.addEventListener('DOMContentLoaded', function() {
    const editCustomerForm = document.getElementById('editCustomerForm');
    const cancelBtn = document.getElementById('cancelBtn');

    if (!editCustomerForm) {
        console.error('Edit customer form not found');
        return;
    }

    const urlParams = new URLSearchParams(window.location.search);
    const custId = urlParams.get('custId');
    const token = localStorage.getItem('token');

    if (!custId) {
        console.error('Customer ID not found in URL');
        return;
    }

    if (!token) {
        console.error('No token found. Redirecting to login.');
        window.location.href = 'login.html';
        return;
    }

    fetch(`http://localhost:8080/${custId}`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => response.json())
    .then(customer => {
        document.getElementById('firstName').value = customer.firstName;
        document.getElementById('lastName').value = customer.lastName;
        document.getElementById('street').value = customer.street;
        document.getElementById('address').value = customer.address || '';
        document.getElementById('city').value = customer.city;
        document.getElementById('state').value = customer.state || '';
        document.getElementById('email').value = customer.email;
        document.getElementById('phone').value = customer.phone;
        
    })
    .catch(error => console.error('Error loading customer data:', error));

    editCustomerForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const updatedCustomer = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            street: document.getElementById('street').value,
            address: document.getElementById('address').value,
            city: document.getElementById('city').value,
            state: document.getElementById('state').value,
            email: document.getElementById('email').value,
            phone: document.getElementById('phone').value,
            password: document.getElementById('password').value
        };

        fetch(`http://localhost:8080/${custId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(updatedCustomer)
        })
        .then(response => response.json())
        .then(() => {
            window.location.href = 'customerlist.html';
        })
        .catch(error => console.error('Error updating customer:', error));
    });

    if (cancelBtn) {
        cancelBtn.addEventListener('click', function() {
            window.location.href = 'customerlist.html';
        });
    }
});
