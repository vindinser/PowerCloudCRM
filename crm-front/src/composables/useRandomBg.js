import bgJpg from '@/assets/bg.jpg'
import mystery1 from '@/assets/mystery/mystery_1.png'
import mystery2 from '@/assets/mystery/mystery_2.png'
import mystery3 from '@/assets/mystery/mystery_3.png'
import mystery4 from '@/assets/mystery/mystery_4.png'

const BG_LIST = [bgJpg, mystery1, mystery2, mystery3, mystery4]

export function getRandomBg() {
  const idx = Math.floor(Math.random() * BG_LIST.length)

  return BG_LIST[idx]
}
