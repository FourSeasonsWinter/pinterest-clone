import { useState } from 'react'
import './Header.css'

export default function Header() {
  const [activeTab, setActiveTab] = useState<number>(0)

  return (
    <nav className='header'>
      <ul>
        <li
          className={activeTab === 0 ? 'active' : ''}
          onClick={() => setActiveTab(0)}
        >
          <span>For you</span>
        </li>
        <li
          className={activeTab === 1 ? 'active' : ''}
          onClick={() => setActiveTab(1)}
        >
          <span>Bedroom interior</span>
        </li>
        <li
          className={activeTab === 2 ? 'active' : ''}
          onClick={() => setActiveTab(2)}
        >
          <span>Hairstyles</span>
        </li>
      </ul>
    </nav>
  )
}
