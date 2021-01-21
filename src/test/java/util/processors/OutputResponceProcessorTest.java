package util.processors;

import Message.abstractions.BinaryMessage;
import abstractions.Condition;
import abstractions.PendingResponces;
import abstractions.ResponceMessage;
import org.junit.Test;
import util.Deps;

import java.io.IOException;

import static abstractions.Condition.DECLINED;
import static org.junit.Assert.*;

public class OutputResponceProcessorTest {
    OutputResponceProcessor orp = new OutputResponceProcessor();
    @Test
    public void saveStatus() throws IOException {
        ResponceMessage res = new ResponceMessage();
        res.ID = "idHelper.getIDusingsimpleID(ID);";
        res.approved = true;
        orp.saveStatus(Deps.PendingResponcesFile, res.ID, DECLINED);
        PendingResponces restored = (PendingResponces) BinaryMessage.restored(BinaryMessage.readBytes(Deps.PendingResponcesFile));
        assertEquals(DECLINED, restored.ReqMap.get(res.ID));

    }


    @Test
    public void printstatuses() throws IOException {
        OutputResponceProcessor orp = new OutputResponceProcessor();
        String out = orp.printstatuses("rendresp.bin");
        assertNotEquals(null, out);

    }
}