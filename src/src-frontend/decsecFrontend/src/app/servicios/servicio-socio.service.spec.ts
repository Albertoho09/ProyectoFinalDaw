import { TestBed } from '@angular/core/testing';

import { ServicioSocioService } from './servicio-socio.service';

describe('ServicioSocioService', () => {
  let service: ServicioSocioService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicioSocioService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
