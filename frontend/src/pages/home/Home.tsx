import './Home.css'
import PinsContainer from '../../components/pins-container/PinsContainer'
import Header from '../../components/header/Header'
import NavBar from '../../components/navbar/NavBar'

export default function Home() {
  return (
    <main>
      <Header />
      <PinsContainer />
      <NavBar />
    </main>
  )
}
