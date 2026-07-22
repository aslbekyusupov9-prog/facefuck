/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        bgStart: '#0d0403',
        bgEnd: '#3a0a08',
        accentRedStart: '#E63946',
        accentRedEnd: '#7F1D1D',
        goldAccent: '#D4AF37',
        goldBright: '#F4C430',
        mintGreen: '#22F0B6',
        cyanMint: '#22D3EE',
        amberScore: '#FFC145',
        amberEnd: '#FF8A3D',
        softCyan: '#38BDF8',
        softPurple: '#818CF8'
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
        heading: ['Manrope', 'sans-serif'],
        mono: ['"IBM Plex Mono"', 'monospace'],
      },
      borderRadius: {
        'phone': '54px',
        'card': '28px',
        'btn': '18px',
      },
      boxShadow: {
        'liquid-glow': 'inset 0 1px 1px 0 rgba(255, 255, 255, 0.25), 0 8px 32px 0 rgba(0, 0, 0, 0.4)',
        'red-pulse': '0 0 25px rgba(230, 57, 70, 0.4)',
        'gold-glow': '0 0 20px rgba(212, 175, 55, 0.3)',
      }
    },
  },
  plugins: [],
}
