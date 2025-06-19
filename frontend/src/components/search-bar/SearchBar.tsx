import { RiSearchLine } from 'react-icons/ri'
import './SearchBar.css'
import { useState } from 'react'

export default function SearchBar() {
  const [active, setActive] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')

  return (
    <div className='search-bar'>
      {!active && <RiSearchLine className='search-icon' />}
      <input
        type='text'
        onClickCapture={() => setActive(true)}
        placeholder='Search for ideas'
        onChange={(e) => setSearchTerm(e.target.value)}
        value={searchTerm}
      />
      {active && (
        <button
          type='reset'
          onClick={() => {
            setActive(false)
            setSearchTerm('')
          }}
        >
          Cancel
        </button>
      )}
    </div>
  )
}
