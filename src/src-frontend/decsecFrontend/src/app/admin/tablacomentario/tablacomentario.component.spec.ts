import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TablacomentarioComponent } from './tablacomentario.component';

describe('TablacomentarioComponent', () => {
  let component: TablacomentarioComponent;
  let fixture: ComponentFixture<TablacomentarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TablacomentarioComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TablacomentarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
