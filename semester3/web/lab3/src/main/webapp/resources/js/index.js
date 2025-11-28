scale = 30;

function main()
{

    document.getElementById('pointForm').addEventListener('submit', e => this.handleSubmit(e));
    document.querySelectorAll('#x-buttons button').forEach(btn => btn.addEventListener('click', e => this.selectX(e)));
    document.getElementById('y').addEventListener('input', e => this.validateY(e));
    document.querySelectorAll('#r-buttons input[type="radio"]').forEach(radio => {
        radio.addEventListener('change', e => this.selectR(e));
    });
    document.getElementById('r').addEventListener('change', e => this.selectR(e));
    document.getElementById('graphCanvas').addEventListener('click', e => this.handleGraphClick(e));
    drawGraphBackground();
}

function selectX(e)
{
    document.querySelectorAll('#x-buttons button').forEach(b => b.classList.remove('selected'));
    e.target.classList.add('selected');
    document.getElementById('x').value = e.target.value;
    this.validateX(e);
}

function validateX(event)
{
    const input = event.target;
    const value = input.value;
    if (!/^-[4321]$|^[01234]$/.test(value)) {
        input.setCustomValidity('Только целые числа из диапазона [-4, 4]');
    } else {
        input.setCustomValidity('');
    }
}

function validateY(event)
{
    const input = event.target;
    const value = input.value.replace(',', '.');
    if (!/^-?[12345]$|^0$|^-?[01234]\.\d+$/.test(value)) {
        input.setCustomValidity('Только числа из диапазона [-5, 5]');
    } else {
        input.setCustomValidity('');
    }
}

function selectR(e)
{
    document.getElementById('r').value = e.target.value;
    this.validateR(e);
}

function validateR(event)
{
    const input = event.target;
    const value = input.value;
    if (!/^[123]$|^[12]\.5$/.test(value)) {
        input.setCustomValidity('R должен быть числом от 1 до 3 с шагом 0.5');
    } else {
        input.setCustomValidity('');
        this.drawGraph();
    }
}

async
function handleSubmit(event)
{
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

function validateData(data)
{
    const x = parseInt(data.x);
    const y = parseFloat(data.y);
    const r = parseInt(data.r);
    return !isNaN(data.x) && !isNaN(data.y) && !isNaN(data.r) && y >= -5 && y <= 5 && [1, 1.5, 2, 2.5, 3].includes(r) && [-4, -3, -2, -1, 0, 1, 2, 3, 4].includes(x);
}

async
function sendData(data)
{
    try {
        const queryParams = new URLSearchParams(data);
        const response = await fetch(`controller?${queryParams}`, {
            method: 'GET',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
        });
        if (response.status === 400) {
            window.location.href = 'https://псж.онлайн';
            throw new Error('ААААААААААА ХАКЕРСКАЯ АТАКА ААААААААААААААААААААААААААА');
        }
        if (!response.ok) throw new Error('Сервер вернул ошибку ' + response.status);
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            const result = await response.json();
            throw result.error;
        } else {
            const html = await response.text();
            const doc = new DOMParser().parseFromString(html, 'text/html');
            document.body.replaceChildren(...doc.body.childNodes);
            const lastResult = document.getElementById('lastResult');
            new PointChecker(true).drawPoint(lastResult);
            const gif = document.getElementById(lastResult.dataset.hit === 'true' ? 'boom-gif' : 'miss-gif');
            gif.style.display = 'block';
            setTimeout(() => {
                gif.style.display = 'none';
            }, lastResult.dataset.hit === 'true' ? 1710 : 1730);
        }
    } catch (error) {
        alert(error.message);
    }
}

function drawPoint(result)
{
    const ctx = document.getElementById('graphCanvas').getContext('2d');
    const w = document.getElementById('graphCanvas').width;
    const h = document.getElementById('graphCanvas').height;
    const centerX = w / 2;
    const centerY = h / 2;
    const x = centerX + (result.dataset.x * this.scale);
    const y = centerY - (result.dataset.y * this.scale);
    const hit = result.dataset.hit === 'true' || result.dataset.hit === true;

    ctx.fillStyle = hit ? '#00ff00' : '#ff0000';
    ctx.beginPath();
    ctx.arc(x, y, 5, 0, Math.PI * 2);
    ctx.fill();
    ctx.lineWidth = 1;
    ctx.strokeStyle = '#000000';
    ctx.stroke();
}

function handleGraphClick(event)
{
    const rect = document.getElementById('graphCanvas').getBoundingClientRect();
    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;
    const w = document.getElementById('graphCanvas').width, h = document.getElementById('graphCanvas').height;
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

    document.querySelectorAll('#x-buttons button').forEach(b => b.classList.remove('selected'));

    if (document.getElementById('pointForm').checkValidity()) {
        this.sendData({x: x, y: y, r: r});
    } else {
        alert('Некорректные координаты точки. Пожалуйста, введите допустимые значения X, Y.');
    }
}

function drawGraphBackground()
{
    const ctx = document.getElementById('bgCanvas').getContext('2d');
    const w = document.getElementById('bgCanvas').width;
    const h = document.getElementById('bgCanvas').height;
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
            ctx.fillText(i, centerX - 15, y);
        }
    };
    if (document.getElementById('r') !== null && document.getElementById('r').value || document.getElementById('lastResult') !== null) this.drawGraph();
}

function drawGraph()
{
    const ctx = document.getElementById('graphCanvas').getContext('2d');
    const w = document.getElementById('graphCanvas').width;
    const h = document.getElementById('graphCanvas').height;
    const centerX = w / 2;
    const centerY = h / 2;
    let r;
    if (document.getElementById('r') !== null) {
        r = document.getElementById('r').value;
    } else if (document.getElementById('lastResult') !== null) {
        r = document.getElementById('lastResult').dataset.r;
    } else return;

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

    // Рисуем точки
    results.filter(result => result.r == r).forEach(result => {
        this.drawPoint({
            dataset: {
                x: result.x,
                y: result.y,
                r: result.r,
                hit: result.hit
            }
        });
    });
}

document.addEventListener('DOMContentLoaded', main());