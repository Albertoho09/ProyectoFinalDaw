import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BotonTemaComponent } from './boton-tema.component';

describe('BotonTemaComponent', () => {
  let component: BotonTemaComponent;
  let fixture: ComponentFixture<BotonTemaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BotonTemaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BotonTemaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
