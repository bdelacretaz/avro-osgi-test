#!/usr/bin/env python

# Test our ping server from Python
# Adapted from the https://github.com/phunt/avro-rpc-quickstart sample
# See http://avro.apache.org/docs/1.7.5/gettingstartedpython.html to install the Python avro libs

import sys
import httplib

import avro.ipc as ipc
import avro.protocol as protocol

PROTOCOL = protocol.parse(open("../../../../rpc-protocol/src/main/avro/ping.avpr").read())

server_addr = ('localhost', 8080, "/avro")

class UsageError(Exception):
    def __init__(self, value):
        self.value = value
    def __str__(self):
        return repr(self.value)

if __name__ == '__main__':
    if len(sys.argv) != 3:
        raise UsageError("Usage: <from> <comments>")

    # client code - attach to the server and send a message
    client = ipc.HTTPTransceiver(server_addr[0], server_addr[1], server_addr[2])
    requestor = ipc.Requestor(PROTOCOL, client)
    
    # fill in the Message record and send it
    message = dict()
    message['from'] = sys.argv[1]
    message['comment'] = sys.argv[2]

    params = dict()
    params['pingMessage'] = message
    print("Result: " + requestor.request('ping', params))

    # cleanup
    client.close()
