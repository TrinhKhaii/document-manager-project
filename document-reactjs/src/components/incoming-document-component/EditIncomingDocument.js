import React from 'react';
import Col from "react-bootstrap/Col";
import {Form, FormGroup, FormText, Input, Label} from "reactstrap";
import {Link, useParams} from "react-router-dom";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import {useEffect, useState} from "react";
import axios from "axios";
import {AutoDismissFlag, FlagGroup} from "@atlaskit/flag";
import SuccessIcon from "@atlaskit/icon/glyph/check-circle";
import {token} from "@atlaskit/tokens";
import {G400} from "@atlaskit/theme/colors";

const EditIncomingDocument = () => {
    const {id} = useParams();
    const editDocumentForm = {
        id: 0,
        excerpt: '',
        serialNumber: '',
        signerId: 0
    }
    const searchSignerForm = {
        pageNumber: 0,
        elementNumber: 1000
    }
    const [document, setDocument] = useState(editDocumentForm);
    const [error, setError] = useState([]);
    const [signerList, setSignerList] = useState([]);
    const [success, setSuccess] = useState([]);

    const handleChange = (event) => {
        const {name, value} = event.target;
        if (name === "signerId") {
            setDocument({...document, [name]: +value});
        } else {
            setDocument({...document, [name]: value});
        }
        setError('');
    }

    const submit = (event) => {
        event.preventDefault();
        axios.post("http://localhost:8080/api/incoming-document/update", document)
            .then((data) => {
                setSuccess(data.data.message);
                addFlag.call();
            }).catch((err) => {
                setError(err.response.data.error);
        })
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

    useEffect(() => {
        axios.post('http://localhost:8080/api/users/list', searchSignerForm)
            .then((data) => {
                setSignerList(data.data.data);
            })
        axios.post("http://localhost:8080/api/incoming-document/find-by-id", {"id": id})
            .then((data) => {
                const document = data.data.data[0];
                setDocument(document);
            })
    }, []);

    const signers = signerList.map(signer => {
        return <option key={signer.id} value={signer.id}>{signer.userName}</option>
    });

    return (
        <>
            <Row className="justify-content-md-center">
                <Col md="auto">
                    <h3>Chỉnh sửa thông tin văn bản</h3>
                    <Form onSubmit={submit}>
                        <FormGroup>
                            <Label for="excerpt">Trích yếu</Label>
                            <Input type="text" name="excerpt" id="excerpt" value={document.excerpt || ''}
                                   onChange={handleChange}/>
                            <FormText className={"text-danger"}>
                                {error.excerpt}
                            </FormText>

                            <Label for="serialNumber">Số hiệu</Label>
                            <Input type="text" name="serialNumber" id="serialNumber" value={document.serialNumber || ''}
                                   onChange={handleChange}/>
                            <FormText className={"text-danger"}>
                                {error.serialNumber}
                            </FormText>

                            <Label for="signerId"> Người ký </Label>
                            <Input type="select" name="signerId" id="signerId" value={document.signerId}
                                   onChange={handleChange}>
                                <option key={0}>--- Chọn người ký ---</option>
                                {signers}
                            </Input>
                            <FormText className={"text-danger"}>
                                {error.signerId}
                            </FormText>
                        </FormGroup>
                        <Link to={"/document-list"}>
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

export default EditIncomingDocument;