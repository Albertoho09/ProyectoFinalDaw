import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthServiceService } from './servicios/auth-service.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthServiceService);
  const router = inject(Router);
  if (authService.loggedIn()) {
    return true;
  }
  router.navigate(['/signin']);
  return false;
};
