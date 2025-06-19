import { IconContext } from 'react-icons'
import './NavBar.css'
import {
  RiHomeSmile2Fill,
  //RiHomeLine,
  RiSearchLine,
  RiChat3Line,
  //RiChat3Fill,
  RiAccountCircleLine,
  //RiAccountCircleFill,
  //RiAddFill,
  RiAddLargeLine,
} from 'react-icons/ri'

export default function NavBar() {
  return (
    <IconContext.Provider value={{ size: "22", color: "black" }}>
      <div className='nav-bar'>
        <div>
          <RiHomeSmile2Fill />
          <span>Home</span>
        </div>
        <div>
          <RiSearchLine />
          <span>Search</span>
        </div>
        <div>
          <RiAddLargeLine />
          <span>Create</span>
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
