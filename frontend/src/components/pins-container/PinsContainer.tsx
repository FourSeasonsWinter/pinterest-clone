import Masonry from 'react-masonry-css'
import type PinModel from '../../models/pin'
import Pin from '../pin/Pin'
import './PinsContainer.css'
import { useEffect, useState } from 'react'
import { fetchPins } from '../../services/pinService'
import InfiniteScroll from 'react-infinite-scroll-component'

export default function PinsContainer() {
  const [pins, setPins] = useState<PinModel[]>([])
  const [page, setPage] = useState<number>(1)
  const [hasMore, setHasMore] = useState<boolean>(true)

  const LIMIT = 15

  const breakpointColumnsObject = {
    default: 8,
    1000: 6,
    900: 5,
    700: 2,
  }

  useEffect(() => {
    async function loadInitialPins() {
      const data = await fetchPins(page, LIMIT)
      setPins(data)
    }

    loadInitialPins()
  }, [])

  async function loadMorePins() {
    const nextPage = page + 1
    const newPins = await fetchPins(nextPage, LIMIT)

    setPins(() => [...pins, ...newPins])
    setPage(nextPage)

    if (newPins.length < LIMIT) {
      setHasMore(false)
    }
  }

  return (
    <InfiniteScroll
      dataLength={pins.length}
      next={loadMorePins}
      hasMore={hasMore}
      loader={<h4>Loading...</h4>}
      endMessage={<p style={{ textAlign: 'center' }}>You have seem it all!</p>}
    >
      <Masonry
        breakpointCols={breakpointColumnsObject}
        className='pins-container'
        columnClassName='pins-container-column'
      >
        {pins.map((pin) => (
          <Pin key={pin.id} pin={pin} />
        ))}
      </Masonry>
    </InfiniteScroll>
  )
}
