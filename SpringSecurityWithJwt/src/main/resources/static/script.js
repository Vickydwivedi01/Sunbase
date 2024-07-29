let customers = [];
document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const addCustomerForm = document.getElementById('addCustomerForm');
    const addCustomerBtn = document.getElementById('addCustomerBtn');
    const syncBtn = document.getElementById('syncBtn');
    const searchInput = document.getElementById('searchInput');

  //  console.log("DOMContentLoaded event triggered");


    if (loginForm) {
        loginForm.addEventListener('submit', function(event) {
            event.preventDefault();

            const loginIdElement = document.getElementById('loginId');
            const passwordElement = document.getElementById('password');

            if (!loginIdElement || !passwordElement) {
                console.error('Login form fields not found');
                return;
            }

            const loginId = loginIdElement.value;
            const password = passwordElement.value;
    
            fetch('http://localhost:8080/signIn', {
                method: 'GET',
                headers: {
                    'Authorization': 'Basic ' + btoa(loginId + ':' + password)
                }
            })
            .then(response => {
                if (response.ok) {
                    let token = response.headers.get('Authorization');
                    if (token && token.startsWith('Bearer ')) {
                        token = token.substring(7); // Remove 'Bearer ' prefix
                    }
                    //const token = response.headers.get('Authorization');
                    // if (token.startsWith('Bearer ')) {
                    //     token = token.substring(7); // Remove 'Bearer ' prefix
                    // }
                    console.log('Received token:', token);
                    if (token) {
                        // Store the token in localStorage 
                        localStorage.setItem('token', token);
                        window.location.href = 'customerlist.html';
                    } else {
                        throw new Error('No token received');
                    }
                } else {
                    throw new Error('Login failed');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Login failed: ' + error.message);
            });
        });
    }


    if (addCustomerForm) {
        addCustomerForm.addEventListener('submit', function(event) {
            event.preventDefault();
            const customer = {
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

            fetch('http://localhost:8080/customers', {
                method: 'POST',
                headers: {
                    // 'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(customer)
            })
            .then(response => response.json())
            .then(data => {
                window.location.href = 'customerlist.html';
            })
            .catch(error => console.error('Error:', error));
        });
    }

    if (addCustomerBtn) {
        addCustomerBtn.addEventListener('click', function() {
            window.location.href = 'addCustomer.html';
        });
    }

    if (syncBtn) {
        syncBtn.addEventListener('click', function() {
            const token = localStorage.getItem('token');
            if (!token) {
                console.error('No token found. Redirecting to login.');
                window.location.href = 'login.html';
                return;
            }
    
            fetch('http://localhost:8080/fetch-and-save-customers', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to sync customers: ' + response.statusText);
                }
                // Check if the response content type is JSON
                const contentType = response.headers.get('Content-Type');
                if (contentType && contentType.includes('application/json')) {
                    return response.json(); // Parse JSON if content type is JSON
                } else {
                    return response.text().then(text => {
                        throw new Error('Unexpected response format: ' + text);
                    });
                }
            })
            .then(data => loadCustomers())
            .catch(error => console.error('Error:', error));
        });
    
    }

    if (searchInput) {
        searchInput.addEventListener('input', function() {
            loadCustomers(searchInput.value);
        });
    }

    function loadCustomers(keyword = '') {
        const token = localStorage.getItem('token');
       // console.log("this is token ....."+token);
        if (!token) {
            console.error('No token found. Redirecting to login.');
            window.location.href = 'login.html';
            return;
        }
        let url = 'http://localhost:8080/customers';
        if (keyword) {
            url = `http://localhost:8080/customers/search?keyword=${encodeURIComponent(keyword)}`;
        }
       // console.log("url...."+url);

       const authHeader = `Bearer ${token}`;
       console.log("Authorization Header:", authHeader);

        fetch(url, {
            method: 'GET',
            headers: {
               // 'Authorization': `Bearer ${token}`
               'Authorization': authHeader
            }
        })
        .then(response => {
            console.log('Response status:', response.status);
            if (!response.ok) {
                if (response.status === 401) {
                    throw new Error('Unauthorized. Please login again.');
                }
                throw new Error('Failed to load customers');
            }
            return response.json();
        })
        .then(data => {
            console.log('Fetched data:', data); 
            customers = data.content;
            console.log('Loaded customers:', customers); // Log loaded customers
            displayCustomers(customers);
        })
        .catch(error => {
            console.error('Error:', error);
            if (error.message === 'Unauthorized. Please login again.') {
                alert(error.message);
                window.location.href = 'index.html';
            } else {
                alert('Failed to load customers: ' + error.message);
            }
        });

        // .then(response => {
        //     if (!response.ok) {
        //         if (response.status === 401) {
        //             throw new Error('Unauthorized. Please login again.');
        //         }
        //         throw new Error('Failed to load customers');
        //     }
        //     return response.json();
        // })
        // .catch(error => {
        //     console.error('Error:', error);
        //     if (error.message === 'Unauthorized. Please login again.') {
        //         alert(error.message);
        //         window.location.href = 'index.html';
        //     } else {
        //         alert('Failed to load customers: ' + error.message);
        //     }
        // });
    }

    function displayCustomers(customers) {
        const customerTableBody = document.querySelector('#customerTable tbody');
        if (!customerTableBody) {
            console.error('Element with ID customerTable tbody not found');
            return;
        }
        customerTableBody.innerHTML = '';
    
        // Check if customers is an array
        if (!Array.isArray(customers)) {
            console.error('Expected customers to be an array, but got:', customers);
            return;
        }
    
        customers.forEach((customer, index) => {
            const customerRow = document.createElement('tr');
    
            customerRow.innerHTML = `
                <td>${customer.firstName}</td>
                <td>${customer.lastName}</td>
                <td>${customer.street}</td>
                <td>${customer.address || ''}</td>
                <td>${customer.city}</td>
                <td>${customer.state || ''}</td>
                <td>${customer.email}</td>
                <td>${customer.phone}</td>
                <td>
                    <button onclick="editCustomer(${index})">EDIT</button>
                    <button onclick="deleteCustomer(${index})">DELETE</button>
                </td>
            `;
    
            customerTableBody.appendChild(customerRow);
        });
    }

    if (window.location.pathname.endsWith('customerlist.html')) {
        loadCustomers();
    }

    window.editCustomer = function(index) {
        const customer = customers[index];
        // Redirect to the edit page with customer ID as a query parameter
        window.location.href = `edit-customer.html?custId=${customer.custId}`;
    };
    
    
    
    window.deleteCustomer = function(index) {
        console.log('Deleting customer at index:', index);
        console.log('Current customers array:', customers);
        if (typeof index !== 'number' || index < 0 || index >= customers.length) {
            console.error('Invalid customer index:', index);
            return;
        }
        const customer = customers[index];
        if (!customer || !customer.custId) {
            console.error('Customer or custId is undefined:', customer);
            return;
        }

        const token = localStorage.getItem('token');
        console.log('Stored token:', token); 
        console.log('Token:', token);
    if (!token) {
        console.error('No token found. Redirecting to login.');
        window.location.href = 'login.html';
        return;
    }

        fetch(`http://localhost:8080/${customer.custId}`, {
            method: 'DELETE',
            headers: {
            'Authorization': `Bearer ${token}`
        }
        })
        .then(response => {
            if (response.ok) {
                loadCustomers(); // Reload the customers list after deletion
            } else {
                throw new Error('Failed to delete customer');
            }
        })
        .catch(error => console.error('Error:', error));
    };

    function logout() {
        localStorage.removeItem('token');
        window.location.href = 'login.html';
    }
});
