/*BBN_LICENSE_START -- DO NOT MODIFY BETWEEN LICENSE_{START,END} Lines
Copyright (c) <2017,2018,2019>, <Raytheon BBN Technologies>
To be applied to the DCOMP/MAP Public Source Code Release dated 2019-03-14, with
the exception of the dcop implementation identified below (see notes).

Dispersed Computing (DCOMP)
Mission-oriented Adaptive Placement of Task and Data (MAP) 

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
BBN_LICENSE_END*/
package com.bbn.protelis.networkresourcemanagement.testbed;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.protelis.lang.datatype.DeviceUID;

import com.bbn.protelis.networkresourcemanagement.NodeLookupService;

/**
 * Assume all nodes are running on localhost. A single instance must be used for
 * all lookups. Each node gets a port number starting at the base port and
 * counting up for each new node that is passed to
 * {@link #getInetAddressForNode(DeviceUID)}. This class is thread-safe.
 * 
 */
public class LocalNodeLookupService implements NodeLookupService {

    private final int basePort;
    private int nextAvailablePort;
    private final Object lock = new Object();
    private final Map<DeviceUID, InetSocketAddress> mapping = new HashMap<>();

    private static final int MIN_NETWORK_PORT = 0;
    private static final int MAX_NETWORK_PORT = 65535;

    /**
     * The base port to use for all Node connections.
     * 
     * @param basePort
     *            a valid network port.
     */
    public LocalNodeLookupService(final int basePort) {
        if (basePort <= MIN_NETWORK_PORT || basePort > MAX_NETWORK_PORT) {
            throw new IllegalArgumentException("Port must be between " + MIN_NETWORK_PORT + " and " + MAX_NETWORK_PORT);
        }
        this.basePort = basePort;
        nextAvailablePort = this.basePort;
    }

    @Override
    public InetSocketAddress getInetAddressForNode(final DeviceUID uid) {
        synchronized (lock) {
            if (mapping.containsKey(uid)) {
                return mapping.get(uid);
            } else {
                final int port = nextAvailablePort;
                final InetSocketAddress addr = new InetSocketAddress(InetAddress.getLoopbackAddress(), port);
                mapping.put(uid, addr);

                ++nextAvailablePort;

                return addr;
            }
        }
    }

}
