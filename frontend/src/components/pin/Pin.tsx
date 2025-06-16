import './Pin.css'

interface Props {
  id: string;
  imageUrl: string;
  description: string;
  userId: string;
}

export default function Pin(props: Props) {
  return (
    <div className="pin">
      <img src={props.imageUrl} alt={props.description} />
    </div>
  );

}