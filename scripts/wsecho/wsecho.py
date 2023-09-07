#!/usr/bin/env python3


import asyncio
import socket
from websockets.server import serve


async def echo(websocket):
    async for message in websocket:
        ip, port = websocket.remote_address
        print(f'Received: {message.strip()}, from {ip}:{port}')
        await websocket.send(message)


async def main():
    async with serve(echo, "0.0.0.0", 8765) as s:
        for socket in s.server.sockets:
            ip, port = socket.getsockname()
            print(f'Listening on {ip}, port {port}')

        await asyncio.Future()  # run forever


def get_ip_address():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    s.settimeout(0)
    try:
        s.connect(('10.254.254.254', 1))
        ip_address = s.getsockname()[0]
    except Exception:
        ip_address = '127.0.0.1'
    finally:
        s.close()
    return ip_address


if __name__ == '__main__':
    ip_addr = get_ip_address()
    print(f'Local IP address: {ip_addr}')

    asyncio.run(main())
