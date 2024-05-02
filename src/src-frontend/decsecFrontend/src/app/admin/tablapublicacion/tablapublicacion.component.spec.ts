import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TablapublicacionComponent } from './tablapublicacion.component';

describe('TablapublicacionComponent', () => {
  let component: TablapublicacionComponent;
  let fixture: ComponentFixture<TablapublicacionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TablapublicacionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TablapublicacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
