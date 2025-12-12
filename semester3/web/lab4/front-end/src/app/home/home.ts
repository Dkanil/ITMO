import {ChangeDetectorRef, Component} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {DatePipe} from '@angular/common';
import {HomeService} from './home.service';
import {Point} from './point';

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
  rValues = [0.5, 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5, 5];
  points: Point[] = [];
  showBoom = false;
  showMiss = false;

  graphCanvas!: HTMLCanvasElement;
  graphCtx!: CanvasRenderingContext2D;
  scale = 30;
  currentRadius = 1;

  constructor(private formBuilder: FormBuilder,
              private homeService: HomeService,
              private router: Router,
              private cdr: ChangeDetectorRef) {
    this.form = this.formBuilder.group({
      x: ['', [Validators.required, Validators.min(-5), Validators.max(5)]],
      y: ['', [Validators.required, Validators.pattern(/^-?\d+([.,]\d+)?$/), Validators.min(-5), Validators.max(5)]],
      r: [this.currentRadius, Validators.required]
    });
    this.form.valueChanges.subscribe(() => {
      this.errorMessage = '';
    });
  }

  ngOnInit() {
    const boomImg = new Image();
    boomImg.src = 'assets/boom.gif';
    const missImg = new Image();
    missImg.src = 'assets/miss.gif';
  }

  ngAfterViewInit() {
    this.graphCanvas = document.getElementById('graphCanvas') as HTMLCanvasElement;
    this.graphCtx = this.graphCanvas?.getContext('2d') as CanvasRenderingContext2D;
    this.getAllPoints();
    this.drawGraphBackground();
  }

  getAllPoints() {
    this.homeService.getAllPoints().subscribe(points => {
      this.points = points;
      this.cdr.detectChanges();
      this.drawGraph();
    });
  }

  submit() {
    if (this.form.invalid) {
      const xControl = this.form.get('x');
      const yControl = this.form.get('y');
      const rControl = this.form.get('r');
      if (xControl?.errors) {
        if (xControl.errors['required']) {
          this.errorMessage = 'Введите координату X\n';
        } else {
          this.errorMessage = 'Координата X должна быть числом в диапазоне -5..5\n';
        }
      }
      if (yControl?.errors) {
        if (yControl.errors['required']) {
          this.errorMessage = 'Введите координату Y\n';
        } else {
          this.errorMessage = 'Координата Y должна быть числом в диапазоне -5..5\n';
        }
      }
      if (rControl?.errors) {
        if (rControl.errors['required']) {
          this.errorMessage = 'Введите радиус R\n';
        }
      }
      return;
    }
    const x = parseFloat(this.form.value.x);
    const y = parseFloat(this.form.value.y);
    const r = parseFloat(this.form.value.r);
    this.homeService.submit(x, y, r).subscribe({
      next: (response) => {
        const point: Point = {
          x: x,
          y: y,
          r: r,
          isHit: response.isHit,
          timestamp: response.timestamp
        };
        this.points.push(point);
        this.drawPoint(point);
        if (point.isHit) {
          this.triggerBoomGif();
        } else {
          this.triggerMissGif();
        }
      },
      error: (error) => {
        this.errorMessage = error.error.message || 'Произошла ошибка при отправке данных.';
      }
    });
  }

  triggerBoomGif() {
    this.showBoom = true;
    this.cdr.detectChanges();
    setTimeout(() => {
      this.showBoom = false;
      this.cdr.detectChanges();
    }, 1710);
  }

  triggerMissGif() {
    this.showMiss = true;
    this.cdr.detectChanges();
    setTimeout(() => {
      this.showMiss = false;
      this.cdr.detectChanges();
    }, 1730);
  }

  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/auth']);
  }

  drawGraphBackground() {
    const bgCanvas = document.getElementById('bgCanvas') as HTMLCanvasElement;
    const bgCtx = bgCanvas?.getContext('2d');
    if (!bgCtx) {
      console.error('Failed to get 2D context for bgCanvas');
      return;
    }
    const w = bgCanvas.width;
    const h = bgCanvas.height;
    const centerX = w / 2;
    const centerY = h / 2;

    const img = new Image();
    img.src = 'assets/graph_background.png';
    img.onload = () => {
      bgCtx.clearRect(0, 0, w, h);
      bgCtx.drawImage(img, 0, 0, w, h);
      bgCtx.save();
      bgCtx.globalAlpha = 0.5;
      bgCtx.fillStyle = "#fff";
      bgCtx.fillRect(0, 0, w, h);
      bgCtx.restore();

      // Оси
      bgCtx.beginPath();
      bgCtx.moveTo(20, centerY);
      bgCtx.lineTo(w - 20, centerY);
      bgCtx.moveTo(centerX, 20);
      bgCtx.lineTo(centerX, h - 20);
      bgCtx.moveTo(centerX - 5, 30);
      bgCtx.lineTo(centerX, 20);
      bgCtx.lineTo(centerX + 5, 30);
      bgCtx.moveTo(w - 30, centerY - 5);
      bgCtx.lineTo(w - 20, centerY);
      bgCtx.lineTo(w - 30, centerY + 5);
      bgCtx.fillText('Y', centerX + 10, 30);
      bgCtx.fillText('X', w - 30, centerY - 10);
      bgCtx.strokeStyle = '#000000';
      bgCtx.stroke();

      for (let i = -4; i < 5; i++) {
        if (i === 0) continue;
        const x = centerX + i * this.scale;
        bgCtx.beginPath();
        bgCtx.moveTo(x, centerY + 5);
        bgCtx.lineTo(x, centerY - 5);
        const y = centerY + i * this.scale;
        bgCtx.moveTo(centerX + 5, y);
        bgCtx.lineTo(centerX - 5, y);
        bgCtx.stroke();
        if (i % 2 === 1 || i % 2 === -1) continue;
        bgCtx.fillText(i.toString(), x, centerY + 15);
        bgCtx.fillText(i.toString(), centerX - 15, y);
      }
    };
  }

  drawGraph() {
    if (!this.graphCtx) {
      console.error('Failed to get 2D context for graphCanvas');
      return;
    }
    const w = this.graphCanvas.width;
    const h = this.graphCanvas.height;
    const centerX = w / 2;
    const centerY = h / 2;

    this.graphCtx.clearRect(0, 0, w, h);
    this.graphCtx.fillStyle = 'rgba(0,208,255,0.8)';

    // 1 четверть
    this.graphCtx.fillRect(centerX, centerY, this.currentRadius * this.scale, - this.currentRadius * this.scale / 2);

    // 2 четверть
    this.graphCtx.beginPath();
    this.graphCtx.moveTo(centerX, centerY);
    this.graphCtx.lineTo(centerX - this.currentRadius * this.scale / 2, centerY);
    this.graphCtx.lineTo(centerX, centerY - this.currentRadius * this.scale / 2);
    this.graphCtx.closePath();
    this.graphCtx.globalAlpha = 0.6;
    this.graphCtx.fill();

    // 3 четверть
    this.graphCtx.beginPath();
    this.graphCtx.moveTo(centerX, centerY);
    this.graphCtx.arc(centerX, centerY, this.currentRadius * this.scale, Math.PI / 2, Math.PI);
    this.graphCtx.closePath();
    this.graphCtx.fill();

    this.graphCtx.globalAlpha = 1.0;

    if (!this.points) {
      console.error('Points data is not available');
      return;
    }
    this.points.filter(result => result.r === this.currentRadius).forEach(result => {
      this.drawPoint(result);
    });
  }

  drawPoint(point: Point) {
    if (!this.graphCtx) {
      console.error('Failed to get 2D context for graphCanvas');
      return;
    }
    const w = this.graphCanvas.width;
    const h = this.graphCanvas.height;
    const centerX = w / 2;
    const centerY = h / 2;
    const x = centerX + (point.x * this.scale);
    const y = centerY - (point.y * this.scale);

    this.graphCtx.fillStyle = point.isHit ? '#00ff00' : '#ff0000';
    this.graphCtx.beginPath();
    this.graphCtx.arc(x, y, 5, 0, Math.PI * 2);
    this.graphCtx.fill();
    this.graphCtx.lineWidth = 1;
    this.graphCtx.strokeStyle = '#000000';
    this.graphCtx.stroke();
  }

  handleRadiusChange() {
    this.currentRadius = parseFloat(this.form.value.r);
    this.drawGraph();
  }

  handleGraphClick(event: MouseEvent) {
    if (!this.graphCanvas) {
      console.error('Failed to get graphCanvas');
      return;
    }
    const rect = this.graphCanvas.getBoundingClientRect();
    const w = this.graphCanvas.width;
    const h = this.graphCanvas.height;
    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;
    const centerX = w / 2;
    const centerY = h / 2;

    const x = ((clickX - centerX) / this.scale).toFixed(5);
    const y = ((centerY - clickY) / this.scale).toFixed(5);

    this.form.patchValue({x: x, y: y});
    this.submit();
  }

}
