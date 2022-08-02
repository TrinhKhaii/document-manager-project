import React, {useEffect} from 'react';
import {Link, useParams} from "react-router-dom";
import axios from "axios";
import Col from "react-bootstrap/Col";
import {Form, FormGroup, FormText, Input, Label} from "reactstrap";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import {useState} from "react";
import {AutoDismissFlag, FlagGroup} from "@atlaskit/flag";
import SuccessIcon from "@atlaskit/icon/glyph/check-circle";
import {token} from "@atlaskit/tokens";
import {G400} from "@atlaskit/theme/colors";

const EditRotation = () => {
    const searchForm = {
        pageNumber: 0,
        elementNumber: 1000
    }
    const {id} = useParams();
    const [error, setError] = useState([]);
    const [success, setSuccess] = useState('');
    const [documentList, setDocumentList] = useState([]);
    const [userList, setUserList] = useState([]);
    const [rotation, setRotation] = useState([]);

    useEffect(() => {
        axios.post("http://localhost:8080/api/rotation/find-by-id", {"id": id})
            .then((data) => {
                console.log(data.data.data[0])
                setRotation(data.data.data[0]);
            })
        axios.post("http://localhost:8080/api/incoming-document/list", searchForm)
            .then((data) => {
                setDocumentList(data.data.data);
            })
        axios.post("http://localhost:8080/api/users/list", searchForm)
            .then((data) => {
                setUserList(data.data.data);
            })
    }, [])

    const handleChange = (event) => {
        const {name, value} = event.target;
        setRotation({...rotation, [name]: +value});
    }

    const submit = (event) => {
        event.preventDefault();
        console.log(rotation)
        axios.post("http://localhost:8080/api/rotation/update", rotation)
            .then((data) => {
                setSuccess(data.data.message);
                addFlag.call();
            }).catch((err) => {
            setError(err.response.data.error);
        })
    }

    const documents = documentList.map(document => {
        return <option key={document.id} value={document.id}>{document.excerpt}</option>
    });

    const users = userList.map(user => {
        return <option key={user.id} value={user.id}>{user.userName}</option>
    })

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
                    <h3>Cập nhật thông tin luân chuyển</h3>
                    <Form onSubmit={submit}>
                        <FormGroup>
                            <Label for="incomingDocumentId">Trích yếu: </Label>
                            <Input type="select" name="incomingDocumentId" id="incomingDocumentId"
                                   onChange={handleChange} value={rotation.incomingDocumentId || ''}>
                                <option key={0} value={0}>--- Chọn văn bản đến ---</option>
                                {documents}
                            </Input>
                            <FormText className={"text-danger"}>
                                {error.incomingDocumentId}
                            </FormText>
                            <br/>
                            <Label for="senderId">Người gửi: </Label>
                            <Input type="select" name="senderId" id="senderId"
                                   onChange={handleChange} value={rotation.senderId || ''}>
                                <option key={0} value={0}>--- Chọn người gửi ---</option>
                                {users}
                            </Input>
                            <FormText className={"text-danger"}>
                                {error.senderId}
                            </FormText>
                            <br/>
                            <Label for="receiverId">Gửi đến: </Label>
                            <Input type="select" name="receiverId" id="receiverId"
                                   onChange={handleChange}  value={rotation.receiverId || ''}>
                                <option key={0} value={0}>--- Chọn người nhận ---</option>
                                {users}
                            </Input>
                            <FormText className={"text-danger"}>
                                {error.receiverId}
                            </FormText>
                        </FormGroup>
                        <Link to={"/rotation-list"}>
                            <Button size="sm" variant="danger">Trở về</Button>
                        </Link>
                        <Button size="sm" variant="primary" type="submit">
                            Cập nhật
                        </Button>
                    </Form>
                </Col>
            </Row>

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

export default EditRotation;