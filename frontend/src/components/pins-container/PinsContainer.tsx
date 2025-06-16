import Masonry from 'react-masonry-css'
import type PinModel from '../../models/pin'
import Pin from '../pin/Pin'
import './PinsContainer.css'

interface Props {
  pins: PinModel[]
}

export default function PinsContainer({ pins }: Props) {
  const breakpointColumnsObject = {
    default: 8,
    1000: 6,
    900: 5,
    700: 2,
  }

  return (
    <Masonry
      breakpointCols={breakpointColumnsObject}
      className='pins-container'
      columnClassName='pins-container-column'
    >
      {pins.map((pin) => (
        <Pin
          key={pin.id}
          id={pin.id}
          imageUrl={pin.imageUrl}
          description={pin.description}
          userId={pin.userId}
        />
      ))}
    </Masonry>
  )
}
