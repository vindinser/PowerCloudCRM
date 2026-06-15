const DPR = Math.min(window.devicePixelRatio || 1, 2)
const MASK_COLOR = '0, 0, 0'
const R_START = 8
const R_END = 180
const R_VARY = 0.45
const LIFETIME = 520
const STAMP_STEP = 12
const MAX_STAMPS = 160

export function useInkMask(containerRef, maskCanvas, { maskAlpha = 0.45 } = {}) {
  let ctx = null
  let w = 0
  let h = 0
  let running = false
  let lastX = null
  let lastY = null
  const stamps = []

  function resizeCanvas() {
    if (!maskCanvas.value || !containerRef.value) {
      return
    }
    const rect = containerRef.value.getBoundingClientRect()

    w = rect.width
    h = rect.height
    const canvas = maskCanvas.value

    canvas.width = Math.round(w * DPR)
    canvas.height = Math.round(h * DPR)
    canvas.style.width = w + 'px'
    canvas.style.height = h + 'px'
    ctx.setTransform(DPR, 0, 0, DPR, 0, 0)
    ctx.globalCompositeOperation = 'source-over'
    ctx.fillStyle = `rgba(${MASK_COLOR}, ${maskAlpha})`
    ctx.fillRect(0, 0, w, h)
  }

  function addStamp(x, y) {
    if (stamps.length >= MAX_STAMPS) {
      stamps.shift()
    }
    stamps.push({
      x,
      y,
      born: performance.now(),
      seed: Math.random() * Math.PI * 2,
      rmax: R_END * (1 - R_VARY + Math.random() * R_VARY)
    })
  }

  function stampAlong(x, y) {
    if (lastX === null) {
      addStamp(x, y)
    } else {
      const dx = x - lastX
      const dy = y - lastY
      const dist = Math.hypot(dx, dy)
      const steps = Math.max(1, Math.ceil(dist / STAMP_STEP))

      for (let i = 1; i <= steps; i++) {
        addStamp(lastX + (dx * i) / steps, lastY + (dy * i) / steps)
      }
    }
    lastX = x
    lastY = y
  }

  function carveInk(x, y, r, alpha, seed) {
    const g = ctx.createRadialGradient(x, y, r * 0.2, x, y, r)

    g.addColorStop(0, `rgba(${MASK_COLOR}, ${0.9 * alpha})`)
    g.addColorStop(0.55, `rgba(${MASK_COLOR}, ${0.88 * alpha})`)
    g.addColorStop(1, `rgba(${MASK_COLOR}, 0)`)
    ctx.fillStyle = g
    ctx.beginPath()
    const segs = 36

    for (let i = 0; i <= segs; i++) {
      const a = (i / segs) * Math.PI * 2
      const wob =
        0.78 +
        0.14 * Math.sin(a * 3 + seed) +
        0.08 * Math.sin(a * 7 + seed * 2.1) +
        0.05 * Math.sin(a * 13 + seed * 0.7)
      const rr = r * wob
      const px = x + Math.cos(a) * rr
      const py = y + Math.sin(a) * rr

      if (i === 0) {
        ctx.moveTo(px, py)
      } else {
        ctx.lineTo(px, py)
      }
    }
    ctx.closePath()
    ctx.fill()
  }

  function loop() {
    const now = performance.now()

    ctx.clearRect(0, 0, w, h)
    ctx.globalCompositeOperation = 'source-over'
    ctx.fillStyle = `rgba(${MASK_COLOR}, ${maskAlpha})`
    ctx.fillRect(0, 0, w, h)
    ctx.globalCompositeOperation = 'destination-out'
    for (let i = stamps.length - 1; i >= 0; i--) {
      const t = (now - stamps[i].born) / LIFETIME

      if (t >= 1) {
        stamps.splice(i, 1)
        continue
      }
      const ease = 1 - Math.pow(1 - t, 3)
      const r = R_START + (stamps[i].rmax - R_START) * ease
      const alpha = 1 - t * t

      carveInk(stamps[i].x, stamps[i].y, r, alpha, stamps[i].seed)
    }
    if (stamps.length) {
      requestAnimationFrame(loop)
    } else {
      running = false
    }
  }

  function startLoop() {
    if (!running) {
      running = true
      requestAnimationFrame(loop)
    }
  }

  function onMouseMove(e) {
    if (!maskCanvas.value) {
      return
    }
    const rect = maskCanvas.value.getBoundingClientRect()

    stampAlong(e.clientX - rect.left, e.clientY - rect.top)
    startLoop()
  }

  function onMouseLeave() {
    lastX = null
    lastY = null
  }

  function init() {
    if (maskCanvas.value) {
      ctx = maskCanvas.value.getContext('2d')
      resizeCanvas()
      window.addEventListener('resize', resizeCanvas)
    }
    document.addEventListener('mousemove', onMouseMove)
    document.addEventListener('mouseleave', onMouseLeave)
  }

  function destroy() {
    document.removeEventListener('mousemove', onMouseMove)
    document.removeEventListener('mouseleave', onMouseLeave)
    window.removeEventListener('resize', resizeCanvas)
  }

  return { init, destroy }
}
