import axios from 'axios';
import type Pin from '../models/pin';

export async function fetchPins(): Promise<Pin[]> {
  try {
    const response = await axios.get(`${import.meta.env.VITE_API_URL}`);
    const formatedPins: Pin[] = response.data.map((pin: any) => ({
      id: crypto.randomUUID(),
      imageUrl: pin.download_url,
      description: '',
      userId: '',
    }));

    return formatedPins;
  } catch (error) {
    console.error('Error fetching pins:', error);
    throw error;
  }
}