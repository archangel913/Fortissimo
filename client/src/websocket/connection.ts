import { onUnmounted } from 'vue'
import SockJS from 'sockjs-client'
import { Client, type IMessage} from '@stomp/stompjs'
import { defineStore } from 'pinia'

let stompClient: Client | null = null

export const useWebsocket = defineStore('websocket', () => {
  const connect = (endpointUrl: string, topicUrl: string, token:string, onRecieve:Function): Promise<void> => {
    return new Promise((resolve, reject) => {
    const socket = new SockJS(endpointUrl)

    stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    })

    stompClient.onConnect = (frame) => {
      console.log('Connected: ' + frame)

      stompClient?.subscribe(topicUrl, (message: IMessage) => {
        if (message.body) {
          onRecieve(JSON.parse(message.body))
        }
      })
      resolve()
    }

    stompClient.onDisconnect = () => {
      console.log('Disconnected')
    }

    stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message'])
    }

    const header = {
      'Authorization': `FFtoken ${token}`
    }
    stompClient.connectHeaders = header;
    
    stompClient.activate();
  });
  }

  const sendMessage = (destination: string, payload: string) => {
    if (stompClient && stompClient.connected) {
      stompClient.publish({
        destination: destination,
        body: payload
      })
    } else {
      console.warn('STOMP client is not connected.')
    }
  }

  const disconnect = () => {
    if (stompClient) {
      stompClient.deactivate()
    }
  }

  onUnmounted(() => {
    disconnect()
  })

  return {
    connect,
    sendMessage,
    disconnect
  }
})