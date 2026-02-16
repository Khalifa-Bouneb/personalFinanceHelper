import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const stored = localStorage.getItem('currentUser');
    if (stored) {
        const user = JSON.parse(stored);
        if (user?.token && !req.url.includes('/api/auth/')) {
            req = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${user.token}`
                }
            });
        }
    }
    return next(req);
};
