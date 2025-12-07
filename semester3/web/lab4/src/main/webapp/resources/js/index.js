scale = 30;
let currentRadius = 1;
document.addEventListener('DOMContentLoaded', drawGraphBackground);
document.getElementById('graphCanvas').addEventListener('click', handleGraphClick);

function setRValue(button) {
    const rPanel = button.parentElement;
    rPanel.querySelectorAll("input[type='submit']")
        .forEach(b => b.classList.remove("selected"));
    button.classList.add("selected");
    currentRadius = parseInt(button.value);
    drawGraph();
}

function drawGraphBackground() {
    const ctx = document.getElementById('bgCanvas').getContext('2d');
    const w = document.getElementById('bgCanvas').width;
    const h = document.getElementById('bgCanvas').height;
    const centerX = w / 2;
    const centerY = h / 2;

    const img = new Image();
    img.src = 'resources/images/1.png';
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
            const x = centerX + i * scale;
            ctx.beginPath();
            ctx.moveTo(x, centerY + 5);
            ctx.lineTo(x, centerY - 5);
            const y = centerY + i * scale;
            ctx.moveTo(centerX + 5, y);
            ctx.lineTo(centerX - 5, y);
            ctx.stroke();
            if (i % 2 === 1 || i % 2 === -1) continue;
            ctx.fillText(i, x, centerY + 15);
            ctx.fillText(i, centerX - 15, y);
        }
    };
    if (currentRadius != null) {
        drawGraph();
    }
}

function drawGraph() {
    const ctx = document.getElementById('graphCanvas').getContext('2d');
    const w = document.getElementById('graphCanvas').width;
    const h = document.getElementById('graphCanvas').height;
    const centerX = w / 2;
    const centerY = h / 2;

    ctx.clearRect(0, 0, w, h);
    ctx.fillStyle = 'rgba(0,208,255,0.8)';

    // 1 четверть
    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.lineTo(centerX + currentRadius * scale / 2, centerY);
    ctx.lineTo(centerX, centerY - currentRadius * scale / 2);
    ctx.closePath();
    ctx.globalAlpha = 0.6;
    ctx.fill();

    // 2 четверть
    ctx.beginPath();
    ctx.moveTo(centerX, centerY);
    ctx.arc(centerX, centerY, currentRadius * scale, Math.PI, Math.PI * 1.5);
    ctx.closePath();
    ctx.fill();

    // 3 четверть
    ctx.fillRect(centerX, centerY, - currentRadius * scale / 2, currentRadius * scale);

    ctx.globalAlpha = 1.0;

    let resultsArray;
    if (!window.results) {
        resultsArray = []
    } else {
        try {
            resultsArray = JSON.parse(window.results);
        } catch (e) {
            console.error('Failed to parse window.results JSON:', e);
            resultsArray = [];
        }
    }
    resultsArray.filter(result => result.r === currentRadius).forEach(result => {
        drawPoint({
            dataset: {
                x: result.x,
                y: result.y,
                r: result.r,
                hit: result.hit
            }
        });
    });
}

function drawPoint(result) {
    const ctx = document.getElementById('graphCanvas').getContext('2d');
    const w = document.getElementById('graphCanvas').width;
    const h = document.getElementById('graphCanvas').height;
    const centerX = w / 2;
    const centerY = h / 2;
    const x = centerX + (result.dataset.x * scale);
    const y = centerY - (result.dataset.y * scale);
    const hit = result.dataset.hit === 'true' || result.dataset.hit === true;

    ctx.fillStyle = hit ? '#00ff00' : '#ff0000';
    ctx.beginPath();
    ctx.arc(x, y, 5, 0, Math.PI * 2);
    ctx.fill();
    ctx.lineWidth = 1;
    ctx.strokeStyle = '#000000';
    ctx.stroke();
}

function showGif(isHit) {
    const gif = document.getElementById(isHit === 'true' ? 'boom-gif' : 'miss-gif');
    gif.style.display = 'block';
    setTimeout(() => {
        gif.style.display = 'none';
    }, isHit === 'true' ? 1710 : 1730);
}

function handleGraphClick(event) {
    const rect = document.getElementById('graphCanvas').getBoundingClientRect();
    const w = document.getElementById('graphCanvas').width;
    const h = document.getElementById('graphCanvas').height;
    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;
    const centerX = w / 2;
    const centerY = h / 2;

    const x = ((clickX - centerX) / scale).toFixed(5);
    const y = ((centerY - clickY) / scale).toFixed(5);
    document.getElementById('pointForm:clickX').value = x;
    document.getElementById('pointForm:clickY').value = y;
    document.getElementById('pointForm:clickR').value = currentRadius;

    document.getElementById('pointForm:hiddenSubmit').click();
}
