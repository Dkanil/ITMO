class PointChecker {
    constructor() {
        this.form = document.getElementById('pointForm');
        this.resultsTable = document.getElementById('table-content');
        this.bgCanvas  = document.getElementById('bgCanvas');
        this.graphCanvas = document.getElementById('graphCanvas');
        this.bgCtx = this.bgCanvas.getContext('2d');
        this.graphCtx = this.graphCanvas.getContext('2d');
        this.xButtons = document.querySelectorAll('#x-buttons button');
        this.rButtons = document.querySelectorAll('#r-buttons input[type="radio"]');
        this.scale = 30;
        this.init();
    }

    init() {
        this.drawGraphBackground();
        this.form.addEventListener('submit', e => this.handleSubmit(e));
        this.xButtons.forEach(btn => btn.addEventListener('click', e => this.selectX(e)));
        document.getElementById('y').addEventListener('input', e => this.validateY(e));
        this.rButtons.forEach(radio => { radio.addEventListener('change', e => this.selectR(e));});
        document.getElementById('r').addEventListener('change', e => this.selectR(e));
        document.getElementById('graphCanvas').addEventListener('click', e => this.handleGraphClick(e));
    }

    selectX(e) {
        this.xButtons.forEach(b => b.classList.remove('selected'));
        e.target.classList.add('selected');
        document.getElementById('x').value = e.target.value;
        this.validateX(e);
    }

    validateX(event) {
        const input = event.target;
        const value = input.value;
        if (!/^-[4321]$|^[01234]$/.test(value)) {
            input.setCustomValidity('Только целые числа из диапазона [-4, 4]');
        } else {
            input.setCustomValidity('');
        }
    }

    validateY(event) {
        const input = event.target;
        const value = input.value.replace(',', '.');
        if (!/^-?[12345]$|^0$|^-?[01234]\.\d+$/.test(value)) {
            input.setCustomValidity('Только числа из диапазона [-5, 5]');
        } else {
            input.setCustomValidity('');
        }
    }

    selectR(e) {
        document.getElementById('r').value = e.target.value;
        this.validateR(e);
    }

    validateR(event) {
        const input = event.target;
        const value = input.value;
        if (!/^[123]$|^[12]\.5$/.test(value)) {
            input.setCustomValidity('R должен быть числом от 1 до 3 с шагом 0.5');
        } else {
            input.setCustomValidity('');
            this.drawGraph();
        }
    }

    async handleSubmit(event) {
        event.preventDefault();
        const data = {
            x: document.getElementById('x').value,
            y: document.getElementById('y').value.replace(',', '.'),
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
        return !isNaN(data.x) && !isNaN(data.y) && !isNaN(data.r) && y >= -5 && y <= 5 && [1, 1.5, 2, 2.5, 3].includes(r) && [-4, -3, -2, -1, 0, 1, 2, 3, 4].includes(x);
    }

    async sendData(data) {
        try {
            const queryParams = new URLSearchParams(data);
            const response = await fetch(`controller?${queryParams}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
            });
            if (response.status === 400) {
                window.location.href = 'https://псж.онлайн';
                throw new Error('ААААААААААА ХАКЕРСКАЯ АТАКА ААААААААААААААААААААААААААА');
            }
            if (!response.ok) throw new Error('Сервер вернул ошибку ' + response.status);
            const result = await response.json();
            if (result.error !== undefined) {
                alert(result.error);
                return;
            }
            this.drawPoint(result);
            this.addResultToTable(result);
            const gif = document.getElementById(result.hit ? 'boom-gif' : 'miss-gif');
            gif.style.display = 'block';
            setTimeout(() => {
                gif.style.display = 'none';
            }, result.hit ? 1710 : 1730);
        } catch (error) {
            alert(error.message);
        }
    }

    addResultToTable(result) {
        const tbody = this.resultsTable.querySelector('tbody');
        const row = document.createElement('tr');

        const date = new Date(result.timestamp).toLocaleString('ru-RU', { hour12: false });
        row.innerHTML = `
            <td>${result.x}</td>
            <td>${result.y}</td>
            <td>${result.r}</td>
            <td>${result.hit ? "Прилёт" : "mOzIlA"}</td>
            <td>${date}</td>
            <td>${result.execution_time}ms</td>
        `;
        tbody.appendChild(row);
    }

    drawPoint(result) {
        const ctx = this.graphCtx;
        const w = this.graphCanvas.width, h = this.graphCanvas.height;
        const centerX = w / 2;
        const centerY = h / 2;
        const r = document.getElementById('r').value;
        if (!r) return;

        const x = centerX + (result.x * this.scale);
        const y = centerY - (result.y * this.scale);

        ctx.fillStyle = result.hit ? '#00ff00' : '#ff0000';
        ctx.beginPath();
        ctx.arc(x, y, 3, 0, Math.PI * 2);
        ctx.fill();

        ctx.lineWidth = 1;
        ctx.strokeStyle = '#000000';
        ctx.stroke();
    }

    handleGraphClick(event) {
        const rect = this.graphCanvas.getBoundingClientRect();
        const clickX = event.clientX - rect.left;
        const clickY = event.clientY - rect.top;
        const w = this.graphCanvas.width, h = this.graphCanvas.height;
        const centerX = w / 2;
        const centerY = h / 2;
        const r = document.getElementById('r').value;
        if (!r) {
            const rInput = document.getElementById('r');
            rInput.setCustomValidity('R должен быть числом от 1 до 3 с шагом 0.5');
            rInput.reportValidity();
            return;
        }

        const x = ((clickX - centerX) / this.scale).toFixed(5);
        const y = ((centerY - clickY) / this.scale).toFixed(5);

        document.getElementById('x').value = x;
        document.getElementById('y').value = y;

        this.xButtons.forEach(b => b.classList.remove('selected'));

        if (document.getElementById('pointForm').checkValidity()) {
            this.sendData({ x: x, y: y, r: r });
        } else {
            alert('Некорректные координаты точки. Пожалуйста, введите допустимые значения X, Y.'); // todo выводить не в alert
        }
    }

    drawGraphBackground() {
        const ctx = this.bgCtx;
        const w = this.bgCanvas.width, h = this.bgCanvas.height;
        const centerX = w / 2;
        const centerY = h / 2;

        const img = new Image();
        img.src = 'resources/1.png';
        img.onload = () => {
            ctx.clearRect(0, 0, w, h);
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

            for (let i = -4; i < 5; i++) {
                if (i === 0) continue;
                const x = centerX + i * this.scale;
                ctx.beginPath();
                ctx.moveTo(x, centerY + 5);
                ctx.lineTo(x, centerY - 5);
                const y = centerY + i * this.scale;
                ctx.moveTo(centerX + 5, y);
                ctx.lineTo(centerX - 5, y);
                ctx.stroke();
                if (i % 2 === 1 || i % 2 === -1) continue;
                ctx.fillText(i, x, centerY + 15);
                ctx.fillText(i, centerX + 10, y);
            }
        };
        if (document.getElementById('r').value) this.drawGraph();
    }

    drawGraph() {
        const ctx = this.graphCtx;
        const w = this.graphCanvas.width, h = this.graphCanvas.height;
        const centerX = w / 2;
        const centerY = h / 2;
        const r = document.getElementById('r').value;
        if (!r) return;

        ctx.clearRect(0, 0, w, h);

        ctx.fillStyle = 'rgba(0,208,255,0.8)';

        // 1 четверть
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.lineTo(centerX + r * this.scale, centerY);
        ctx.lineTo(centerX, centerY - r * this.scale);
        ctx.closePath();
        ctx.globalAlpha = 0.6;
        ctx.fill();

        // 2 четверть
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, r * this.scale / 2, Math.PI, Math.PI * 1.5);
        ctx.closePath();
        ctx.fill();

        // 4 четверть
        ctx.fillRect(centerX, centerY, r * this.scale, r * this.scale / 2);

        ctx.globalAlpha = 1.0;
    }
}

document.addEventListener('DOMContentLoaded', () => new PointChecker());