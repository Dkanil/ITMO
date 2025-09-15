class PointChecker {
    constructor() {
        this.form = document.getElementById('pointForm');
        this.canvas = document.getElementById('graphCanvas');
        this.ctx = this.canvas.getContext('2d');
        this.resultsTable = document.getElementById('table-content');
        this.xButtons = document.querySelectorAll('#x-buttons button');
        this.init();
    }

    init() {
        this.drawGraph();
        this.loadResults();
        this.form.addEventListener('submit', e => this.handleSubmit(e));
        this.xButtons.forEach(btn => btn.addEventListener('click', e => this.selectX(e)));
        document.getElementById('y').addEventListener('input', e => this.validateY(e));
        document.getElementById('r').addEventListener('input', e => this.validateR(e));
    }

    selectX(e) {
        this.xButtons.forEach(b => b.classList.remove('selected'));
        e.target.classList.add('selected');
        document.getElementById('x').value = e.target.value;
        this.validateX();
    }

    validateX() {
        const input = document.getElementById('x');
        const value = input.value;
        if (!/^-[321]$|^[012345]$/.test(value)) {
            input.setCustomValidity('КОВЫРЯТЬСЯ В КОДЕ ЭЛЕМЕНТА ПЛОХО.');
        } else {
            input.setCustomValidity('');
        }
    }

    validateY(event) {
        const input = event.target;
        const value = input.value.replace(',', '.');
        if (!/^-?[12]$|^0$|^-?[012]\.\d+$/.test(value)) {
            input.setCustomValidity('Только числа из диапазона (-3, 3)');
        } else {
            input.setCustomValidity('');
        }
    }

    validateR(event) {
        const input = event.target;
        const value = input.value;
        if (!/^[1-5]$/.test(value)) {
            input.setCustomValidity('R должен быть натуральным числом от 1 до 5');
        } else {
            input.setCustomValidity('');
        }
    }

    async handleSubmit(event) {
        event.preventDefault();
        const data = {
            x: document.getElementById('x').value,
            y: document.getElementById('y').value,
            r: document.getElementById('r').value
        };
        if (!this.validateData(data)) {
            alert('кОд ЭлЕмЕнТа мЕнЯтЬ пЛоХо');
            return;
        }
        await this.sendData(data);
    }

    validateData(data) {
        const x = parseInt(data.x);
        const y = parseFloat(data.y);
        const r = parseInt(data.r);
        return !isNaN(x) && !isNaN(y) && !isNaN(r) && y > -3 && y < 3 && [1, 2, 3, 4, 5].includes(r) && [-3, -2, -1, 0, 1, 2, 3, 4, 5].includes(x);
    }

    async sendData(data) {
        try {
            console.log('Trying to send data:', data);
            const response = await fetch("/fcgi-bin/lab1.jar", {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams(data)
            });
            console.log('Response got:', response);
            if (!response.ok) throw new Error('Сервер вернул ошибку ' + response.status);
            const result = await response.json();
            if (result.error !== undefined) {
                alert(result.error);
                return;
            }
            const gif = document.getElementById(result.hit ? 'boom-gif' : 'miss-gif');
            gif.style.display = 'block';
            setTimeout(() => {
                gif.style.display = 'none';
            }, result.hit ? 1710 : 1730);
            this.addResultToTable(result);
            this.saveResult(result);
        } catch (error) {
            alert(error.message);
        }
    }

    addResultToTable(result) {
        const tbody = this.resultsTable.querySelector('tbody');
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${result.x}</td>
            <td>${result.y}</td>
            <td>${result.r}</td>
            <td>${result.hit ? "Прилёт" : "mOzIlA"}</td>
            <td>${new Date(result.timestamp).toLocaleString()}</td>
            <td>${result.execution_time}ms</td>
        `;
        tbody.appendChild(row);
    }

    saveResult(result) {
        const results = this.getStoredResults();
        results.push(result);
        localStorage.setItem('pointResults', JSON.stringify(results));
    }

    loadResults() {
        const results = this.getStoredResults();
        results.forEach(result => this.addResultToTable(result));
    }

    getStoredResults() {
        return JSON.parse(localStorage.getItem('pointResults') || '[]');
    }

    drawGraph() {
        const ctx = this.ctx;
        const w = this.canvas.width, h = this.canvas.height;
        const centerX = w / 2;
        const centerY = h / 2;

        const img = new Image();
        img.src = '1.png';
        img.onload = () => {
            ctx.drawImage(img, 0, 0, w, h);
            ctx.save();
            ctx.globalAlpha = 0.5;
            ctx.fillStyle = "#fff";
            ctx.fillRect(0, 0, w, h);
            ctx.restore();

            // Оси
            ctx.beginPath();
            ctx.moveTo(20, centerY);
            ctx.lineTo(w - 20, centerY);
            ctx.moveTo(centerX, 20);
            ctx.lineTo(centerX, h - 20);
            ctx.moveTo(centerX - 5, 30);
            ctx.lineTo(centerX, 20);
            ctx.lineTo(centerX + 5, 30);
            ctx.moveTo(w - 30, centerY - 5);
            ctx.lineTo(w - 20, centerY);
            ctx.lineTo(w - 30, centerY + 5);
            ctx.fillText('Y', centerX + 10, 30);
            ctx.fillText('X', w - 30, centerY - 10);
            ctx.strokeStyle = '#000000';
            ctx.stroke();

            for (let i = -2; i < 3; i++) {
                if (i === 0) continue;
                const x = centerX + i * 50;
                ctx.beginPath();
                ctx.moveTo(x, centerY + 5);
                ctx.lineTo(x, centerY - 5);
                const y = centerY + i * 50;
                ctx.moveTo(centerX + 5, y);
                ctx.lineTo(centerX - 5, y);
                ctx.stroke();
                if (i === -1 || i === 1) {
                    ctx.fillText("R/2", x, centerY + 15);
                    ctx.fillText("R/2", centerX + 15, y);
                } else {
                    ctx.fillText("R", x, centerY + 15);
                    ctx.fillText("R", centerX + 15, y);
                }
            }

            ctx.fillStyle = '#7bff00';

            // 1 четверть
            ctx.beginPath();
            ctx.moveTo(centerX, centerY);
            ctx.arc(centerX, centerY, 50, Math.PI * 1.5, 0);
            ctx.closePath(); // Замыкаем путь
            ctx.globalAlpha = 0.6;
            ctx.fill();

            // 2 четверть
            ctx.fillRect(centerX, centerY, 100, 50);

            // 3 четверть
            ctx.beginPath();
            ctx.moveTo(centerX, centerY);
            ctx.lineTo(centerX - 50, centerY);
            ctx.lineTo(centerX, centerY + 50);
            ctx.closePath();
            ctx.fill();

            ctx.globalAlpha = 1.0;
        };
    }
}

document.addEventListener('DOMContentLoaded', () => new PointChecker());