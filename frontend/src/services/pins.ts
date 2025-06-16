import axios from 'axios';
import type PinModel from '../models/pin';

function generateRandomHeight(): number {
  return Math.floor(Math.random() * (400 - 200 + 1)) + 200; // Random height between 200 and 400
}

export async function fetchPins(): Promise<PinModel[]> {
  try {
    const response = await axios.get(`${import.meta.env.VITE_API_URL}`);
    const formatedPins: PinModel[] = response.data.map((pin: any) => ({
      id: crypto.randomUUID(),
      imageUrl: `https://picsum.photos/id/${pin.id}/300/${generateRandomHeight()}`,
      description: '',
      userId: '',
    }));

    return formatedPins;
  } catch (error) {
    console.error('Error fetching pins:', error);
    throw error;
  }
}