import {ChangeDetectorRef, Component} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {DatePipe} from '@angular/common';
import {HomeService} from './home.service';

@Component({
  selector: 'app-home',
  imports: [FormsModule, ReactiveFormsModule, DatePipe],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class HomeComponent {
  form: FormGroup;
  errorMessage = '';
  xValues = [-2, -1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2];
  rValues = [-0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5];
  points: Array<{ x: number, y: number, r: number, isHit: boolean, timestamp: string }> = [];
  showBoom = false;
  showMiss = false;

  constructor(private formBuilder: FormBuilder,
              private homeService: HomeService,
              private router: Router,
              private cdr: ChangeDetectorRef) {
    this.form = this.formBuilder.group({
      x: ['', Validators.required],
      y: ['', Validators.required],
      r: ['', Validators.required]
    });
    this.form.valueChanges.subscribe(() => {
      this.errorMessage = '';
    });
  }

  ngOnInit() {
    this.getAllPoints();
  }

  getAllPoints() {
    this.homeService.getAllPoints().subscribe(points => {
      this.points = points;
    });
  }

  submit() {
    // todo
    this.triggerBoomGif();
  }

  triggerBoomGif() {
    this.showBoom = true;
    setTimeout(() => {
      this.showBoom = false;
      this.cdr.detectChanges();
    }, 1710);
  }

  triggerMissGif() {
    this.showMiss = true;
    setTimeout(() => {
      this.showMiss = false;
      this.cdr.detectChanges();
    }, 1730);
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/auth']);
  }
}
