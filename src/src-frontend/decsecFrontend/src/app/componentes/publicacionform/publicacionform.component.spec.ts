import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicacionformComponent } from './publicacionform.component';

describe('PublicacionformComponent', () => {
  let component: PublicacionformComponent;
  let fixture: ComponentFixture<PublicacionformComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PublicacionformComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PublicacionformComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
