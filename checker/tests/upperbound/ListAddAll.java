import java.util.*;
import org.checkerframework.checker.index.qual.*;
import org.checkerframework.checker.upperbound.qual.*;

class ListAddAll {

    List<Integer> listField;
    List<Integer> coll;

    void ListAddAll(
            @LTLengthOf("#3") int index, @LTEqLengthOf("#3") int notIndex, List<Integer> list) {
        list.addAll(index, coll);

        //:: warning: (list.access.unsafe.high)
        list.addAll(notIndex, coll);
    }

    int[] arr = {0};

    void ListAddAllWrongName(@LTLengthOf("arr") int index, List<Integer> list) {
        //:: warning: (list.access.unsafe.high)
        list.addAll(index, coll);
    }

    void ListAddAllField() {
        listField.addAll(listField.size() - 1, coll);
        listField.addAll(this.listField.size() - 1, coll);
        this.listField.addAll(listField.size() - 1, coll);
        this.listField.addAll(this.listField.size() - 1, coll);

        //:: warning: (list.access.unsafe.high)
        listField.addAll(listField.size(), coll);
        //:: warning: (list.access.unsafe.high)
        listField.addAll(this.listField.size(), coll);
        //:: warning: (list.access.unsafe.high)
        this.listField.addAll(listField.size(), coll);
        //:: warning: (list.access.unsafe.high)
        this.listField.addAll(this.listField.size(), coll);
    }

    void ListAddAllFieldUserAnnotation(@IndexFor("listField") int i) {
        listField.addAll(i, coll);
        this.listField.addAll(i, coll);

        //:: warning: (list.access.unsafe.high)
        listField.addAll(i + 1, coll);
        //:: warning: (list.access.unsafe.high)
        this.listField.addAll(i + 1, coll);
    }

    void ListAddAllUserAnnotation(@IndexFor("#2") int i, List<Integer> list) {
        list.addAll(i, coll);

        //:: warning: (list.access.unsafe.high)
        list.addAll(i + 1, coll);
    }
}
