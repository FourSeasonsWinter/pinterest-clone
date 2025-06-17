import type PinModel from '../../models/pin';
import './Pin.css'

interface Props {
  pin: PinModel;
}

export default function Pin({ pin }: Props) {
  return (
    <div className="pin">
      <img src={pin.imageUrl} alt={pin.description} loading='lazy' />
    </div>
  );

}