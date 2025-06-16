import { useEffect, useState } from 'react'
import { fetchPins } from '../../services/pins'
import './Home.css'
import type Pin from '../../models/pin'

export default function Home() {
  const [pins, setPins] = useState<Pin[]>([])
  const [loading, setLoading] = useState<boolean>(true)

  useEffect(() => {
    async function getPins() {
      try {
        const data = await fetchPins()
        setPins(data)

        for (const pin of data) {
          console.log(`Pin ID: ${pin.id}, Image URL: ${pin.imageUrl}`)
        }
      } catch (error) {
        console.error('Error fetching pins:', error)
      } finally {
        setLoading(false)
      }
    }

    getPins()
  }, [])

  if (loading) {
    return <div>Loading...</div>
  }

  return (
    <div className='pins-container'>
      {pins.map((pin) => (
        <div key={pin.id} className='pin'>
          <img src={pin.imageUrl} alt={pin.description} />
          <p>{pin.description}</p>
        </div>
      ))}
    </div>
  )
}
