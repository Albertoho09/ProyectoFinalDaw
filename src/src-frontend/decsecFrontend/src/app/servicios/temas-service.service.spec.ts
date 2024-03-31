import { TestBed } from '@angular/core/testing';

import { TemasServiceService } from './temas-service.service';

describe('TemasServiceService', () => {
  let service: TemasServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TemasServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
