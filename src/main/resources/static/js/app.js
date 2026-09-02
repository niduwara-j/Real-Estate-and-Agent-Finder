// Haven Real Estate - Interactive App & Dark/Light Mode Engine

// Theme Controller
const initTheme = () => {
    const savedTheme = localStorage.getItem('haven_theme') || 
        (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
    applyTheme(savedTheme);
};

const applyTheme = (theme) => {
    document.documentElement.setAttribute('data-bs-theme', theme);
    localStorage.setItem('haven_theme', theme);
    
    const themeIcon = document.getElementById('themeIcon');
    if (themeIcon) {
        if (theme === 'dark') {
            themeIcon.className = 'bi bi-sun-fill text-warning';
        } else {
            themeIcon.className = 'bi bi-moon-stars-fill text-primary';
        }
    }
};

document.addEventListener('DOMContentLoaded', () => {
    // Initialize Theme
    initTheme();

    const toggleBtn = document.getElementById('themeToggleBtn');
    if (toggleBtn) {
        toggleBtn.addEventListener('click', () => {
            const currentTheme = document.documentElement.getAttribute('data-bs-theme') || 'light';
            applyTheme(currentTheme === 'dark' ? 'light' : 'dark');
        });
    }

    // Auto-dismiss alert messages after 5 seconds
    const alerts = document.querySelectorAll('.alert-dismissible');
    alerts.forEach(alert => {
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });

    // Client-side quick filter for Agent List
    const agentSearchInput = document.getElementById('quickAgentSearch');
    if (agentSearchInput) {
        agentSearchInput.addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase();
            const agentCards = document.querySelectorAll('.agent-card-item');
            agentCards.forEach(card => {
                const text = card.textContent.toLowerCase();
                card.style.display = text.includes(query) ? '' : 'none';
            });
        });
    }

    // Interactive Star Rating Selector
    const starInputs = document.querySelectorAll('.star-rating-select input');
    starInputs.forEach(input => {
        input.addEventListener('change', (e) => {
            const ratingValue = e.target.value;
            const ratingText = document.getElementById('selectedRatingText');
            if (ratingText) {
                ratingText.textContent = `${ratingValue} Star${ratingValue > 1 ? 's' : ''}`;
            }
        });
    });
});

// Confirmation Dialog Helper
function confirmAction(message, formId) {
    if (confirm(message)) {
        document.getElementById(formId).submit();
    }
}
