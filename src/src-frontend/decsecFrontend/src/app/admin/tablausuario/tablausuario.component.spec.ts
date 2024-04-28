import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TablausuarioComponent } from './tablausuario.component';

describe('TablausuarioComponent', () => {
  let component: TablausuarioComponent;
  let fixture: ComponentFixture<TablausuarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TablausuarioComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TablausuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
