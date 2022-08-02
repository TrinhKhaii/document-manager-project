import React, {useEffect, useState} from 'react';
import Col from "react-bootstrap/Col";
import {Form, FormGroup, FormText, Input, Label} from "reactstrap";
import {Link} from "react-router-dom";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import axios from "axios";
import Modal from 'react-bootstrap/Modal';
import {MDBTable, MDBTableBody, MDBTableHead} from "mdb-react-ui-kit";
import {AutoDismissFlag, FlagGroup} from "@atlaskit/flag";
import SuccessIcon from "@atlaskit/icon/glyph/check-circle";
import {token} from "@atlaskit/tokens";
import {G400} from "@atlaskit/theme/colors";

const AddRotation = () => {
    const addRotationForm = {
        incomingDocumentId: 0,
        senderId: 0,
        receiverName: [0]
    }
    const searchForm = {
        pageNumber: 0,
        elementNumber: 1000
    }
    const [documentList, setDocumentList] = useState([]);
    const [userList, setUserList] = useState([]);
    const [error, setError] = useState([]);
    const [success, setSuccess] = useState('');
    const [show, setShow] = useState(false);
    const [checked, setChecked] = useState([]);
    const [receiverNameTerm, setReceiverNameTerm] = useState([]);
    const [receiverNameList, setReceiverNameList] = useState([]);
    const [rotation, setRotation] = useState(addRotationForm);

    useEffect(() => {
        axios.post("http://localhost:8080/api/incoming-document/list", searchForm)
            .then((data) => {
                setDocumentList(data.data.data);
            })
        axios.post("http://localhost:8080/api/users/list", searchForm)
            .then((data) => {
                setUserList(data.data.data);
            })
    }, [])

    const documents = documentList.map(document => {
        return <option key={document.id} value={document.id}>{document.excerpt}</option>
    });

    const users = userList.map(user => {
        return <option key={user.id} value={user.id}>{user.userName}</option>
    })

    const handleCheck = (id, userName) => {
        setChecked(prevState => {
            const isChecked = checked.includes(id);
            if (isChecked) {
                setReceiverNameTerm(prevState1 => {
                    return receiverNameTerm.filter(item => item !== userName);
                })
                return checked.filter(item => item !== id);
            } else {
                setReceiverNameTerm(prevState1 => {
                    return [...prevState1, userName];
                })
                return [...prevState, id];
            }
        });
    }

    const receivers = userList.map(user => {
        return <tr key={user.id}>
            <td><input type="checkbox" checked={checked.includes(user.id)} onChange={() => handleCheck(user.id, user.userName)}/></td>
            <td>{user.id}</td>
            <td>{user.userName}</td>
        </tr>
    });

    const handleModalClose = () => {
        setChecked([]);
        setReceiverNameTerm([]);
        setShow(false)
    };

    const handleModalShow = () => setShow(true);

    const chooseReceiver = (event) => {
        event.preventDefault();
        handleModalShow();
    }

    const addReceiverToList = () => {
        setReceiverNameList(receiverNameTerm);
        setRotation({...rotation, receiverId: checked})
        setShow(false)
    }

    const receiverChooseList = receiverNameList.map(receiver => {
        return <p className={"text-primary"} key={receiver}> {receiver} </p>
    })

    const submit = (event) => {
        event.preventDefault();
        axios.post("http://localhost:8080/api/rotation/save-multi", rotation)
            .then((data) => {
                setSuccess(data.data.message);
                setRotation([]);
                setError([]);
                addFlag.call();
            }).catch((err) => {
            setError(err.response.data.error);

        })
    }

    const handleChange = (event) => {
        const {name, value} = event.target;
        setRotation({...rotation, [name]: +value});
        setError([]);
    }

    const [flags, setFlags] = React.useState([]);

    const addFlag = () => {
        const newFlagId = flags.length + 1;
        const newFlags = flags.slice();
        newFlags.splice(0, 0, newFlagId);
        setFlags(newFlags);
    };

    const handleDismiss = () => {
        setFlags(flags.slice(1));
    };

    return (
        <>
            <Row className="justify-content-md-center">
                <Col md="auto">
                    <h3>Thêm mới luân chuyển</h3>
                    <Form onSubmit={submit}>
                        <FormGroup>
                            <Label for="incomingDocumentId">Trích yếu: </Label>
                            <Input type="select" name="incomingDocumentId" id="incomingDocumentId"
                                   onChange={handleChange}>
                                <option key={0}>--- Chọn văn bản đến ---</option>
                                {documents}
                            </Input>
                            <FormText className={"text-danger"}>
                                {error.incomingDocumentId}
                            </FormText>
                            <br/>
                            <Label for="senderId">Người gửi: </Label>
                            <Input type="select" name="senderId" id="senderId"
                                   onChange={handleChange}>
                                <option key={0}>--- Chọn người gửi đến ---</option>
                                {users}
                            </Input>
                            <FormText className={"text-danger"}>
                                {error.senderId}
                            </FormText>
                            <br/>
                            <Label for="receiverId">Gửi đến: </Label>
                            {receiverChooseList}
                            <Button size="sm" variant="primary" type="submit" onClick={chooseReceiver}>
                                Chọn người đến
                            </Button>
                            {/*<FormText className={"text-danger"}>*/}
                            {/*    {error.receiverId}*/}
                            {/*</FormText>*/}


                        </FormGroup>
                        <Link to={"/rotation-list"}>
                            <Button size="sm" variant="danger">Trở về</Button>
                        </Link>
                        <Button size="sm" variant="primary" type="submit">
                            Thêm mới
                        </Button>
                    </Form>
                </Col>
            </Row>

            <Modal show={show} onHide={handleModalClose}>
                <Modal.Header>
                    <Modal.Title>Chọn người gửi</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <MDBTable striped hover>
                        <MDBTableHead>
                            <tr>
                                <th scope='col'>#</th>
                                <th scope='col'>Id</th>
                                <th scope='col'>Tên</th>
                            </tr>
                        </MDBTableHead>
                        <MDBTableBody>
                            {receivers}
                        </MDBTableBody>
                    </MDBTable>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="danger" onClick={handleModalClose}>
                        Hủy
                    </Button>
                    <Button variant="primary" onClick={addReceiverToList}>
                        Xác nhận
                    </Button>
                </Modal.Footer>
            </Modal>

            <FlagGroup onDismissed={handleDismiss}>
                {flags.map((flagId) => {
                    return (
                        <AutoDismissFlag
                            appearance="success"
                            id={flagId}
                            icon={
                                <SuccessIcon
                                    label="Success"
                                    secondaryColor={token('color.background.success.bold', G400)}
                                />
                            }
                            key={flagId}
                            title={success}
                        />
                    );
                })}
            </FlagGroup>
        </>
    );
};

export default AddRotation;