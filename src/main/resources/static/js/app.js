// Frontend Application Logic for Clothing CRM

document.addEventListener('DOMContentLoaded', function() {
    
    // Auto-dismiss alert boxes after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function() {
                alert.remove();
            }, 500);
        }, 5000);
    });

    // Checkout page dynamic pricing calculator
    const productSelect = document.getElementById('order-product-select');
    const quantityInput = document.getElementById('order-quantity');
    const orderTotalVal = document.getElementById('order-total-value');

    if (productSelect && quantityInput && orderTotalVal) {
        function calculateTotal() {
            const selectedOption = productSelect.options[productSelect.selectedIndex];
            const price = parseFloat(selectedOption.getAttribute('data-price') || 0);
            const quantity = parseInt(quantityInput.value || 0);
            const total = price * quantity;
            
            // Format currency in USD
            orderTotalVal.textContent = '$' + total.toFixed(2);
        }

        productSelect.addEventListener('change', calculateTotal);
        quantityInput.addEventListener('input', calculateTotal);
        
        // Initial calculation
        calculateTotal();
    }

    // Reports & Analytics page Charts
    const salesChartCanvas = document.getElementById('salesChart');
    const statusChartCanvas = document.getElementById('statusChart');

    if (salesChartCanvas && typeof Chart !== 'undefined') {
        // Mock data for weekly sales analytics
        new Chart(salesChartCanvas, {
            type: 'line',
            data: {
                labels: ['Dushanba', 'Seshanba', 'Chorshanba', 'Payshanba', 'Juma', 'Shanba', 'Yakshanba'],
                datasets: [{
                    label: 'Ulgurji Savdo ($)',
                    data: [12000, 19000, 15000, 25000, 22000, 30000, 28000],
                    borderColor: '#6366f1',
                    backgroundColor: 'rgba(99, 102, 241, 0.1)',
                    tension: 0.4,
                    fill: true,
                    borderWidth: 3,
                    pointBackgroundColor: '#6366f1'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        grid: {
                            color: 'rgba(255, 255, 255, 0.05)'
                        },
                        ticks: {
                            color: '#94a3b8'
                        }
                    },
                    x: {
                        grid: {
                            display: false
                        },
                        ticks: {
                            color: '#94a3b8'
                        }
                    }
                }
            }
        });
    }

    if (statusChartCanvas && typeof Chart !== 'undefined') {
        // Fetch values from hidden elements or datasets
        const pending = parseInt(statusChartCanvas.getAttribute('data-pending') || 0);
        const processing = parseInt(statusChartCanvas.getAttribute('data-processing') || 0);
        const completed = parseInt(statusChartCanvas.getAttribute('data-completed') || 0);

        new Chart(statusChartCanvas, {
            type: 'doughnut',
            data: {
                labels: ['Kutilmoqda', 'Jarayonda', 'Bajarildi'],
                datasets: [{
                    data: [pending, processing, completed],
                    backgroundColor: ['#f59e0b', '#3b82f6', '#10b981'],
                    borderColor: '#1e293b',
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            color: '#94a3b8',
                            font: {
                                family: 'Outfit'
                            }
                        }
                    }
                },
                cutout: '70%'
            }
        });
    }
});
