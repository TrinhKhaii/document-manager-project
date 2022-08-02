import React from 'react';
import Col from "react-bootstrap/Col";
import {Form, FormGroup, FormText, Input, Label} from "reactstrap";
import {Link} from "react-router-dom";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import {useState, useEffect} from "react";
import axios from "axios";
import {AutoDismissFlag, FlagGroup} from "@atlaskit/flag";
import SuccessIcon from "@atlaskit/icon/glyph/check-circle";
import {token} from "@atlaskit/tokens";
import {G400} from "@atlaskit/theme/colors";

const AddIncomingDocument = () => {
    const addDocumentForm = {
        excerpt: '',
        serialNumber: '',
        signerId: 0
    }
    const searchSignerForm = {
        pageNumber: 0,
        elementNumber: 1000
    }
    const [document, setDocument] = useState(addDocumentForm);
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

    const submit = async (event) => {
        event.preventDefault();
        await axios.post("http://localhost:8080/api/incoming-document/save", document)
            .then((data) => {
                setSuccess(data.data.message);
                setDocument(addDocumentForm);
                setError([]);
                addFlag.call();
            }).catch((err) => {

                setError(err.response.data.error);
            })
    }

    useEffect(() => {
        axios.post('http://localhost:8080/api/users/list', searchSignerForm)
            .then((data) => {
                setSignerList(data.data.data);
            })
    }, []);

    const signers = signerList.map(signer => {
        return <option key={signer.id} value={signer.id}>{signer.userName}</option>
    });

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
                    <h3>Thêm mới văn bản</h3>
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
                            <Input type="select" name="signerId" id="signerId" onChange={handleChange}>
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
                            Thêm mới
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

export default AddIncomingDocument;