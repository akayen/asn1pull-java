/***** BEGIN LICENSE BLOCK *****
* Version: CPL 1.0/GPL 2.0/LGPL 2.1
*
* The contents of this file are subject to the Common Public
* License Version 1.0 (the "License"); you may not use this file
* except in compliance with the License. You may obtain a copy of
* the License at http://www.eclipse.org/legal/cpl-v10.html
*
* Software distributed under the License is distributed on an "AS
* IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
* implied. See the License for the specific language governing
* rights and limitations under the License.
*
* Copyright (C) 2011 Martin Bosslet <Martin.Bosslet@googlemail.com>
*
* Alternatively, the contents of this file may be used under the terms of
* either of the GNU General Public License Version 2 or later (the "GPL"),
* or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
* in which case the provisions of the GPL or the LGPL are applicable instead
* of those above. If you wish to allow use of your version of this file only
* under the terms of either the GPL or the LGPL, and not to allow others to
* use your version of this file under the terms of the CPL, indicate your
* decision by deleting the provisions above and replace them with the notice
* and other provisions required by the GPL or the LGPL. If you do not delete
* the provisions above, a recipient may use your version of this file under
* the terms of any one of the CPL, the GPL or the LGPL.
 */
package org.jruby.ext.krypt.asn1.encode;

import org.jruby.ext.krypt.asn1.SerializationException;
import java.io.IOException;
import java.io.OutputStream;
import org.jruby.ext.krypt.asn1.Asn1;
import org.jruby.ext.krypt.asn1.Constructed;
import org.jruby.ext.krypt.asn1.Primitive;


/**
 * 
 * @author <a href="mailto:Martin.Bosslet@googlemail.com">Martin Bosslet</a>
 */
public class Asn1Serializer {
    
    private Asn1Serializer() {}
    
    public static void serialize(Asn1 asn, OutputStream out) {
        if (asn.getHeader().isConstructed()) 
            serializeConstructed((Constructed<?>)asn, out);
        else 
            serializePrimitive((Primitive)asn, out);
    }
    
    private static void serializeConstructed(Constructed<?> c, OutputStream out) {
        c.getHeader().encodeTo(out);
        for (Asn1 asn : c.getContent()) {
            serialize(asn, out);
        }
    }
    
    private static void serializePrimitive(Primitive p, OutputStream out) {
        try {
            p.getHeader().encodeTo(out);
            byte[] value = p.getValue();
            if (value != null)
                out.write(value);
        }
        catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }
    
}
