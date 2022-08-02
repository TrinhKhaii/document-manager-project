import React, {useEffect} from 'react';
import {useState} from "react";
import axios from "axios";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";
import {Input, Label} from "reactstrap";
import Button from "react-bootstrap/Button";
import {Link} from "react-router-dom";
import {MDBTable, MDBTableBody, MDBTableHead} from "mdb-react-ui-kit";
import Pagination from "react-bootstrap/Pagination";
import Modal from "react-bootstrap/Modal";
import {AutoDismissFlag, FlagGroup} from "@atlaskit/flag";
import SuccessIcon from "@atlaskit/icon/glyph/check-circle";
import {token} from "@atlaskit/tokens";
import {G400} from "@atlaskit/theme/colors";

const RotationList = () => {
    const searchParamForm = {
        excerpt: '',
        senderName: '',
        receiverName: '',
        fromDate: '2000-01-01 00:00:00',
        toDate: '3000-01-01 23:59:59',
        pageNumber: 0,
        elementNumber: 4
    }
    const searchForm = {
        pageNumber: 0,
        elementNumber: 1000
    }
    const [rotationList, setRotationList] = useState([]);
    const [searchParam, setSearchParam] = useState(searchParamForm);
    const [searchParamVal, setSearchParamVal] = useState(searchParamForm);
    const [page, setPage] = useState();
    const [totalPage, setTotalPage] = useState();
    const [show, setShow] = useState(false);
    const [deleteRotation, setDeleteRotation] = useState({'id': 0, 'excerpt': '', 'senderName': '', 'receiverName': ''});
    const [deleteSuccess, setDeleteSuccess] = useState([]);
    const [show2, setShow2] = useState(false);
    const [documentList, setDocumentList] = useState([]);
    const [documentId, setDocumentId] = useState(0);

    const searchHandleChange = (event) => {
        const {name, value} = event.target;
        if (name === "fromDate" || name === "toDate") {
            const dateValue = new Intl.DateTimeFormat('en-ZA', {
                month: '2-digit',
                day: '2-digit',
                year: 'numeric',
                minute: '2-digit',
                hour: '2-digit',
                second: '2-digit',
            }).format(new Date(value)).replace(',', '').replace('/', '-').replace('/', '-');
            setSearchParamVal({...searchParamVal, [name]: dateValue});
        } else {
            setSearchParamVal({...searchParamVal, [name]: value});
        }
    }

    const search = () => {
        setSearchParam(searchParamVal);
    }

    useEffect(() => {
        axios.post("http://localhost:8080/api/rotation/list-dto", searchParam)
            .then((data) => {
                setRotationList(data.data.data);
                setPage(data.data.page);
                setTotalPage(Math.ceil(data.data.totalElement / data.data.numberOfElementPerPage));
            })
    }, [searchParam])

    useEffect(() => {
        axios.post("http://localhost:8080/api/incoming-document/list", searchForm)
            .then((data) => {
                setDocumentList(data.data.data);
            })
    }, []);

    const previousPage = () => {
        setSearchParam({...searchParam, pageNumber: page - 1});
    }

    const nextPage = () => {
        setSearchParam({...searchParam, pageNumber: page + 1});
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

    const callComfirmDeleteModal = (id, excerpt, senderName, receiverName) => {
        setDeleteRotation({id: id, excerpt: excerpt, senderName: senderName, receiverName: receiverName});
        handleConfirmDeleteModalShow();
    }

    const handleConfirmDeleteModalClose = () => setShow(false);
    const handleConfirmDeleteModalShow = () => setShow(true);

    const handleDeleteUser = (id) => {
        axios.post("http://localhost:8080/api/rotation/delete", {"id": deleteRotation.id})
            .then((data) => {
                setDeleteSuccess(data.data.message);
                setDeleteRotation({id: 0, excerpt: '', senderName: '', receiverName: ''});
                setSearchParam(searchParamForm);
                handleConfirmDeleteModalClose();
                addFlag.call();
            })
    }

    const resetSearch = () => {
        setSearchParam(searchParamForm)
    }

    const handleModalClose = () => setShow2(false);
    const handleModalShow = () => setShow2(true);

    const handleChangeDocumentId = (event) => {
        const { value } = event.target;
        setDocumentId(value);
    }

    const handleGetList = () => {
        console.log(documentId)
    }

    const documents = documentList.map(document => {
        return <option key={document.id} value={document.id}>{document.excerpt}</option>
    })

    const rotations = rotationList.map(rotation => {
        return <tr key={rotation.id}>
            <td>{rotation.id}</td>
            <td>{rotation.incomingDocumentExcerpt}</td>
            <td>{rotation.senderName}</td>
            <td>{rotation.receiverName}</td>
            <td>
                {new Intl.DateTimeFormat('en-GB', {
                    month: '2-digit',
                    day: '2-digit',
                    year: 'numeric',
                    minute: '2-digit',
                    hour: '2-digit',
                    second: '2-digit',
                }).format(new Date(rotation.deliveryDate))}
            </td>
            <td>
                <Link to={"/rotation-edit/" + rotation.id}>
                    <Button size="sm" variant="outline-warning">Sửa</Button>
                </Link>
                <Button size="sm" variant="outline-danger"
                        onClick={() => callComfirmDeleteModal(rotation.id, rotation.incomingDocumentExcerpt, rotation.senderName, rotation.receiverName)}>Xóa</Button>
            </td>
        </tr>
    });

    return (
        <>
            <Row>
                <Col lg={{span: 4, offset: 1}}>
                    <h3>Danh sách luân chuyển</h3>
                </Col>
                <Col lg={{span: 2, offset: 1}}>
                    <Label for="excerptSearch">Trích yếu của văn bản</Label>
                    <Input type="text" name="excerpt" id="excerptSearch"
                           onChange={searchHandleChange}/>

                    <Label for="senderNameSearch">Người gửi</Label>
                    <Input type="text" name="senderName" id="senderNameSearch"
                           onChange={searchHandleChange}/>

                    <Label for="receiverNameSearch">Người nhận</Label>
                    <Input type="text" name="receiverName" id="receiverNameSearch"
                           onChange={searchHandleChange}/>

                    <Label for="fromDateSearch">Từ ngày</Label>
                    <Input type="datetime-local" name="fromDate" id="fromDateSearch"
                           onChange={searchHandleChange}/>

                    <Label for="toDateSearch">Đến ngày</Label>
                    <Input type="datetime-local" name="toDate" id="toDateSearch"
                           onChange={searchHandleChange}/>

                </Col>
                <Col lg={{span: 2, offset: 1}}>
                    <Button size="sm" variant="primary" type="submit" onClick={search}>
                        Tìm kiếm
                    </Button>
                    <Button size="sm" variant="primary" type="submit" onClick={resetSearch}>
                        Reset
                    </Button>
                </Col>

            </Row>
            <Row className="justify-content-md-center">
                <Col md="auto">
                    <Link to={"/rotation-add"}>
                        <Button size="sm" variant="outline-success">Thêm mới</Button>
                    </Link>
                    <Button size="sm" variant="outline-success" onClick={handleModalShow}>Thống kê danh sách luân chuyển của văn bản</Button>
                    <MDBTable striped hover>
                        <MDBTableHead>
                            <tr>
                                <th scope='col'>Id</th>
                                <th scope='col'>Trích yếu văn bản đến</th>
                                <th scope='col'>Người gửi</th>
                                <th scope='col'>Người nhận</th>
                                <th scope='col'>Ngày gửi</th>
                                <th scope='col'>Hành động</th>
                            </tr>
                        </MDBTableHead>
                        <MDBTableBody>
                            {rotations}
                        </MDBTableBody>
                    </MDBTable>
                </Col>
            </Row>
            <Row>
                <Col></Col>
                <Col md="auto"></Col>
                <Col xs lg="3">
                    <Pagination>
                        {
                            page > 0
                                ? <Pagination.Prev onClick={previousPage}/>
                                : <Pagination.Prev disabled/>
                        }
                        <Pagination.Item>{page + 1}</Pagination.Item>
                        <Pagination.Item>{"/"}</Pagination.Item>
                        <Pagination.Item>{totalPage}</Pagination.Item>
                        {
                            page < totalPage - 1
                                ? <Pagination.Next onClick={nextPage}/>
                                : <Pagination.Next disabled/>
                        }
                    </Pagination>
                </Col>
            </Row>

            <Modal
                show={show}
                onHide={handleConfirmDeleteModalClose}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header>
                    <Modal.Title>Xác nhận</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Bạn chắc chắn muốn xóa luân chuyển của văn bản có trích yếu là
                    <span className="text-danger"> {deleteRotation.excerpt} </span> được gửi từ
                    người dùng <span className="text-danger"> {deleteRotation.senderName} </span> đến người dùng
                    <span className="text-danger"> {deleteRotation.receiverName} </span>.
                    <p className="text-warning"> Lưu ý: Thao tác này không thể hoàn tác </p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="primary" onClick={handleConfirmDeleteModalClose}>
                        Đóng
                    </Button>
                    <Button variant="danger" onClick={() => handleDeleteUser()}>Xóa</Button>
                </Modal.Footer>
            </Modal>

            <Modal
                show={show2}
                onHide={handleModalClose}
                backdrop="static"
                keyboard={false}
            >
                <Modal.Header>
                    <Modal.Title>Chọn văn bản đến</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Input type="select" name="incomingDocumentId" id="incomingDocumentId"
                           onChange={handleChangeDocumentId}>
                        <option key={0} value={0}>--- Chọn văn bản đến ---</option>
                        {documents}
                    </Input>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="danger" onClick={handleModalClose}>
                        Đóng
                    </Button>
                    <Link to={"/rotation-list-of-document/" + documentId}>
                        <Button variant="primary">Xác nhận</Button>
                    </Link>
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
                            title={deleteSuccess}
                        />
                    );
                })}
            </FlagGroup>
        </>
    );
};

export default RotationList;