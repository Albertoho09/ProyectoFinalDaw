import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CardloginComponent } from './cardlogin.component';

describe('CardloginComponent', () => {
  let component: CardloginComponent;
  let fixture: ComponentFixture<CardloginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardloginComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CardloginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
