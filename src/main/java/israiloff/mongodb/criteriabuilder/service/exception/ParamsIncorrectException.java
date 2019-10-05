package israiloff.mongodb.criteriabuilder.service.exception;


import israiloff.mongodb.criteriabuilder.service.enumiration.ErrorEnum;

public class ParamsIncorrectException extends javax.management.JMRuntimeException {
    //region Constructors

    public ParamsIncorrectException() {
        super(ErrorEnum.WRONG_PARAMS.toString());
    }

    //endregion
}
