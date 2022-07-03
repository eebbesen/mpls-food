document.querySelectorAll('.js-submit-confirm').forEach(($item) => {
    $item.addEventListener('submit', (event) => {
        if (!confirm(event.currentTarget.getAttribute('data-confirm-message'))) {
            event.preventDefault();
            return false;
        }
        return true;
    });
});

document.querySelectorAll('.js-dropdown').forEach(($item) => {
    $item.addEventListener('click', (event) => {
        document.querySelectorAll('.js-dropdown').forEach(($dropdown) => {
            if (event.currentTarget === $dropdown ||
                    ($dropdown.getAttribute('data-dropdown-single') !== 'true' && $dropdown.ariaExpanded === 'true')) {
                $dropdown.ariaExpanded = $dropdown.ariaExpanded !== 'true';
                $dropdown.nextElementSibling.classList.toggle('hidden');
            }
        });
        return false;
    });
});
