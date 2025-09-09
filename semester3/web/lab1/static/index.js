class PointChecker {
    constructor() {
        this.form = document.getElementById('pointForm');
        this.canvas = document.getElementById('graphCanvas');
        this.ctx = this.canvas.getContext('2d');
        this.resultsTable = document.getElementById('resultsTable');
        this.xButtons = document.querySelectorAll('#x-buttons button');
        this.xInput = document.getElementById('x');
        this.init();
    }

    init() {
        this.drawGraph();
        this.loadResults();
        this.form.addEventListener('submit', e => this.handleSubmit(e));
        this.xInput.addEventListener('input', e => this.validateX(e));
        document.getElementById('y').addEventListener('input', e => this.validateY(e));
        document.getElementById('r').addEventListener('input', e => this.validateR(e));
        this.xButtons.forEach(btn => btn.addEventListener('click', e => this.selectX(e)));
    }

    selectX(e) {
        this.xButtons.forEach(b => b.classList.remove('selected'));
        e.target.classList.add('selected');
        this.xInput.value = e.target.value;
    }

    validateX(event) {
        const input = event.target;
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
        const value = input.value.replace(',', '.');
        if (!/^[1-5]$/.test(value)) {
            input.setCustomValidity('R должен быть натуральным числом от 1 до 5');
        } else {
            input.setCustomValidity('');
        }
    }

    async handleSubmit(event) {
        event.preventDefault();
        const data = {
            x: this.xInput.value,
            y: document.getElementById('y').value,
            r: document.getElementById('r').value
        };
        if (!this.validateData(data)) {
            alert('Проверьте данные');
            return;
        }
        console.log('Trying to send data:', data);
        await this.sendData(data);
    }

    validateData(data) {
        const y = parseFloat(data.y);
        const r = parseFloat(data.r);
        return !isNaN(y) && !isNaN(r) && y >= -3 && y <= 3 && r >= 1 && r <= 5;
    }

    async sendData(data) {
        try {
            const response = await fetch("/fcgi-bin/app.jar", {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams(data)
            });
            console.log('Response got:', response);
            if (!response.ok) throw new Error('Ошибка пока не обрабатывается');
            const result = await response.json();
            this.addResultToTable(result);
            this.saveResult(result);
        } catch (error) {
            alert(error.message);
        }
    }

    addResultToTable(result) {
        const row = this.resultsTable.insertRow(-1);
        row.innerHTML = `
            <td>${result.x}</td>
            <td>${result.y}</td>
            <td>${result.r}</td>
            <td>${result.hit}</td>
            <td>${new Date(result.timestamp).toLocaleString()}</td>
            <td>${result.execution_time}ms</td>
        `;
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
        ctx.clearRect(0, 0, w, h);

        // Оси
        ctx.beginPath();
        ctx.moveTo(20, centerY);
        ctx.lineTo(w - 20, centerY);
        ctx.moveTo(centerX, 20);
        ctx.lineTo(centerX, h - 20);
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
            ctx.lineTo(centerX  - 5, y);
            ctx.stroke();
            if (i === -1 || i === 1) {
                ctx.fillText("R/2", x, centerY + 15);
                ctx.fillText("R/2", centerX + 15, y);
            }
            else {
                ctx.fillText("R", x, centerY + 15);
                ctx.fillText("R", centerX + 15, y);
            }
        }

        ctx.fillStyle = '#007cff';

        // 1 четверть
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, 50, Math.PI * 1.5, 0);
        ctx.closePath(); // Замыкаем путь
        ctx.globalAlpha = 0.5;
        ctx.fill();

        // 2 четверть
        ctx.fillRect(centerX, centerY,  100, 50);

        // 3 четверть
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX - 50, centerY);
        ctx.lineTo(centerX, centerY + 50);
        ctx.closePath();
        ctx.fill();

        ctx.globalAlpha = 1.0;
    }
}

document.addEventListener('DOMContentLoaded', () => new PointChecker());