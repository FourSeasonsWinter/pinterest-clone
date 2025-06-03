import { IconContext } from 'react-icons'
import './NavBar.css'
import {
  RiHomeSmile2Fill,
  RiHomeLine,
  RiSearchLine,
  RiChat3Line,
  RiChat3Fill,
  RiAccountCircleLine,
  RiAccountCircleFill,
} from 'react-icons/ri'

export default function NavBar() {
  return (
    <IconContext.Provider value={{ size: "26", color: "#0008" }}>
      <div className='nav-bar'>
        <div>
          <RiHomeLine />
          <span>Home</span>
        </div>
        <div>
          <RiSearchLine />
          <span>Search</span>
        </div>
        <div>
          <RiChat3Line />
          <span>Inbox</span>
        </div>
        <div>
          <RiAccountCircleLine />
          <span>Saved</span>
        </div>
      </div>
    </IconContext.Provider>
  )
}
