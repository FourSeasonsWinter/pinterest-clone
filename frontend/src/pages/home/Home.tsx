import { useEffect, useState } from 'react'
import { fetchPins } from '../../services/pins'
import './Home.css'
import type PinModel from '../../models/pin'
import PinsContainer from '../../components/pins-container/PinsContainer'

export default function Home() {
  const [pins, setPins] = useState<PinModel[]>([])
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
    <PinsContainer pins={pins} />
  )
}
