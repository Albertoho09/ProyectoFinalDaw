import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardUsuarioAdminComponent } from './card-usuario-admin.component';

describe('CardUsuarioAdminComponent', () => {
  let component: CardUsuarioAdminComponent;
  let fixture: ComponentFixture<CardUsuarioAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardUsuarioAdminComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CardUsuarioAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
