import GoogleButton from '../../components/google/GoogleButton'
import './Login.css'
import { FaPinterest } from 'react-icons/fa'

export default function Login() {
  return (
    <div className='login-page'>
      <div className='background'></div>
      <FaPinterest size={44} color='white' />
      <h1>
        Welcome to
        <br />
        Pinterest
      </h1>
      <button className='email'>Continue with email</button>
      <GoogleButton />

      <b>Already a member? Log in</b>
      <p>
        This application is a clone of Pinterest, created solely for learning
        purposes in full stack development.{' '}
        <a href='https://github.com/FourSeasonsWinter/pinterest-clone'>
          See on GitHub
        </a>
        .
      </p>
    </div>
  )
}
